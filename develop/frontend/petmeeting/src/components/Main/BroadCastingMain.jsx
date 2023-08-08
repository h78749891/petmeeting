import React, { useState, useEffect } from 'react';
import { Swiper, SwiperSlide } from 'swiper/react';
import { Card, CardContent, Typography, Box } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import 'swiper/css';
import 'swiper/css/effect-coverflow';
import 'swiper/css/pagination';
import 'swiper/css/navigation';
import { EffectCoverflow, Pagination, Navigation } from 'swiper';
import axios from 'axios';
import { OpenVidu } from 'openvidu-browser';
import { useDispatch } from 'react-redux';
import { setSessionInstance, setSubscribers } from '../../stores/Slices/sessionSlice';

const APPLICATION_SERVER_URL = 'https://i9a203.p.ssafy.io/openvidu/';
const OPENVIDU_PASSWORD = process.env.REACT_APP_OPENVIDU_PASSWORD;


function BroadCastingMain() {
    const navigate = useNavigate();
    const dispatch = useDispatch();

    const [videoTitles, setVideoTitles] = useState({});
    const [videoThumbnails, setVideoThumbnails] = useState({});
    const [videoDescriptions, setVideoDescriptions] = useState({});
    const customSessionId = "GodJaeHong"
    const mySession = "Sessionjaehong4"

    const liveBroadcasts = [{ id: "BZcu8MK_jfo" }, { id: "zwVAKBO8rJM" }, { id: "uqkhMBJ9yrs" }];

    // OpenVidu 관련 상태 변수
    // const [mytoken, setmytoken] = useState(null);
    const [subscribers, setSubscriberss] = useState([]);

    const initializeSession = () => {
      const OV = new OpenVidu();
      // console.log('OV 잘만들어짐? : ',OV)
      const sessionInstance = OV.initSession();
      // console.log('Session 잘 만들어짐? : ', sessionInstance)


      sessionInstance.on('streamCreated', (event) => {
        // console.log('구독구독')
          const subscriber = sessionInstance.subscribe(event.stream, JSON.parse(event.stream.connection.data).clientData);
          setSubscriberss(prevSubscribers => [...prevSubscribers, subscriber]);
          console.log(subscriber)
          var temp = []
          temp.push(subscriber)
          dispatch(setSubscribers(temp))
      });

      
      console.log(sessionInstance)
      console.log('Session내용: ', sessionInstance)
      return sessionInstance
    };

    const joinSessionSub = async () => {
      const sessionInstance = await initializeSession();
      dispatch(setSessionInstance(sessionInstance));
      console.log('join1')
      const token = await getToken();
      // console.log('session(Join에서):',sessionInstance)
      // console.log('token 잘 들어옴?', token)
      sessionInstance.connect(token, { clientData: mySession })
          .then(async() => {
              console.log('Successfully connected to the session as a subscriber');
              // console.log('넘겨줄 세션:',sessionInstance)
              navigate(`/broadcasting/${customSessionId}`, {
                state: {
                    title: "OpenVidu Live Session",
                    description: "This is an OpenVidu live streaming session.",
                    thumbnail: "path_to_dummy_thumbnail.jpg",
                    isLiveSession: true,
                    token: token // 직접 token 변수를 사용
                }
              });
          })
          .catch((error) => {
              console.log('There was an error connecting to the session:', error.code, error.message);
          });
      // setmytoken(token)
    };

    const getToken = async() => {
      // console.log('gettoken1')
      const sessionId = await createSession(customSessionId);
      return await createToken(sessionId);
    }
    const createSession = (sessionId) => {
      return new Promise((resolve, reject) => {
        let data = JSON.stringify({ customSessionId: sessionId });
    
        axios
          .post(`${APPLICATION_SERVER_URL}api/sessions`, data, {
            headers: {
              Authorization: `Basic ${btoa(`OPENVIDUAPP:${OPENVIDU_PASSWORD}`)}`,
              'Content-Type': 'application/json',
            },
          })
          .then((res) => {
            resolve(res.data.id);
          })
          .catch((res) => {
            let error = Object.assign({}, res);
    
            if (error?.response?.status === 409) {
              resolve(sessionId);
            } else if (
              window.confirm(
                'No connection to OpenVidu Server. This may be a certificate error at "' +
                  APPLICATION_SERVER_URL +
                  '"\n\nClick OK to navigate and accept it. If no certificate warning is shown, then check that your OpenVidu Server is up and running at "' +
                  APPLICATION_SERVER_URL +
                  '"'
              )
            ) {
              window.location.assign(APPLICATION_SERVER_URL + '/accept-certificate');
            }
          });
      });
    };

    const createToken = (sessionId) => {
      return new Promise((resolve, reject) => {
        let data = {};
    
        axios
          .post(
            `${APPLICATION_SERVER_URL}api/sessions/${sessionId}/connection`,
            data,
            {
              headers: {
                Authorization: `Basic ${btoa(`OPENVIDUAPP:${OPENVIDU_PASSWORD}`)}`,
                'Content-Type': 'application/json',
              },
            }
          )
          .then((res) => {
            resolve(res.data.token);
          })
          .catch((error) => reject(error));
      });
    };

    const handleCardClick = (broadcastId) => {
        navigate(`/broadcasting/${broadcastId}`, {
            state: {
                title: videoTitles[broadcastId],
                description: videoDescriptions[broadcastId],
                thumbnail: videoThumbnails[broadcastId],
                isLiveSession: false,
            }
        });
    };

    const handleOpenViduClick = async() => {
      joinSessionSub()
    };
  

    useEffect(() => {
        const API_KEY = "AIzaSyB1Wdv8X-6SZJFgtNRh-JD1VkeLjTNCFKc";

        const fetchVideoDetails = async () => {
            const cachedData = localStorage.getItem('videoDetails');
            if (cachedData) {
                const { titles, thumbnails, descriptions } = JSON.parse(cachedData);
                setVideoTitles(titles);
                setVideoThumbnails(thumbnails);
                setVideoDescriptions(descriptions);
                console.log('로컬스토리지에서 받아옴');
                return;
            }

            try {
                const videoIds = liveBroadcasts.map(broadcast => broadcast.id).join(',');
                const response = await axios.get(`https://www.googleapis.com/youtube/v3/videos?id=${videoIds}&key=${API_KEY}&part=snippet`);

                let fetchedTitles = {};
                let fetchedThumbnails = {};
                let fetchedDescriptions = {};

                response.data.items.forEach(item => {
                    fetchedTitles[item.id] = item.snippet.title;
                    fetchedThumbnails[item.id] = item.snippet.thumbnails.high.url;
                    fetchedDescriptions[item.id] = item.snippet.description;
                });

                setVideoTitles(fetchedTitles);
                setVideoThumbnails(fetchedThumbnails);
                setVideoDescriptions(fetchedDescriptions);

                localStorage.setItem('videoDetails', JSON.stringify({
                    titles: fetchedTitles,
                    thumbnails: fetchedThumbnails,
                    descriptions: fetchedDescriptions
                }));

            } catch (e) {
                console.log('제목 가져오기 에러 : ', e);
            }
        };

        fetchVideoDetails();
    }, []);

    return (
        <Box className="container" sx={{ mt: 1 }} style={{ maxWidth: '932px' }}>
            <Typography variant="h6" gutterBottom className="heading">
                Live Broadcast Previews
            </Typography>
            <Swiper
                effect={'coverflow'}
                grabCursor={true}
                centeredSlides={true}
                loop={false}
                slidesPerView={'auto'}
                coverflowEffect={{
                    rotate: 0,
                    stretch: 0,
                    depth: 100,
                    modifier: 2.5,
                }}
                pagination={{ el: '.swiper-pagination', clickable: true }}
                navigation={{
                    nextEl: '.swiper-button-next',
                    prevEl: '.swiper-button-prev',
                    clickable: true,
                }}
                modules={[EffectCoverflow, Pagination, Navigation]}
                className="swiper_container"
            >
                {/* OpenVidu 세션의 SwiperSlide */}
                <SwiperSlide style={{ width: '500px', height: '350px' }}>
                    <Card onClick={handleOpenViduClick} style={{ height: '100%' }}>
                        <Box display="flex" flexDirection="column" height="100%">
                            <Box
                                display="flex"
                                justifyContent="center"
                                alignItems="center"
                                style={{
                                    flexGrow: 5,
                                    width: '100%',
                                    backgroundImage: `url(path_to_dummy_thumbnail.jpg)`,
                                    backgroundSize: 'cover',
                                    backgroundPosition: 'center'
                                }}
                            >
                            </Box>
                            <CardContent style={{ flexGrow: 1, display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
                                <Typography variant="Jua">OpenVidu Live Session</Typography>
                            </CardContent>
                        </Box>
                    </Card>
                </SwiperSlide>

                {/* 기존의 라이브 스트리밍 SwiperSlides */}
                {liveBroadcasts.map((broadcast) => (
                    <SwiperSlide key={broadcast.id} style={{ width: '500px', height: '350px' }}>
                      <h1>asdf</h1>
                        <Card onClick={() => handleCardClick(broadcast.id)} style={{ height: '100%' }}>
                            <Box display="flex" flexDirection="column" height="100%">
                                <Box
                                    display="flex"
                                    justifyContent="center"
                                    alignItems="center"
                                    style={{
                                        flexGrow: 5,
                                        width: '100%',
                                        backgroundImage: `url(${videoThumbnails[broadcast.id]})`,
                                        backgroundSize: 'cover',
                                        backgroundPosition: 'center'
                                    }}
                                >
                                </Box>
                                <CardContent style={{ flexGrow: 1, display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
                                    <Typography variant="Jua">{videoTitles[broadcast.id] || "Loading..."}</Typography>
                                </CardContent>
                            </Box>
                        </Card>
                    </SwiperSlide>
                ))}

                <div className="slider-controler">
                    <div className="swiper-button-prev slider-arrow" style={{ top: '50%', left: '10px' }}>
                        <ion-icon name="arrow-back-outline"></ion-icon>
                    </div>
                    <div className="swiper-button-next slider-arrow" style={{ top: '50%', right: '10px' }}>
                        <ion-icon name="arrow-forward-outline"></ion-icon>
                    </div>
                </div>
                <div className="swiper-pagination"></div>
            </Swiper>
        </Box>
    );
}

export default BroadCastingMain;