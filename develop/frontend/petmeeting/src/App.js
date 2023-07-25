import React, {useState} from 'react';
import { BrowserRouter as Router, Route, Routes, Link, useLocation } from 'react-router-dom';
import { Provider, useSelector } from 'react-redux';
import store from './stores/index';
import { AppBar, Button, Toolbar, Typography, Grid, Box, Menu, MenuItem } from '@mui/material'
import AccountCircleIcon from '@mui/icons-material/AccountCircle'
import MainPage from './pages/MainPage';
import ShelterPage from './pages/Shelter';
import AdoptionPage from './pages/Adoption';
import BoardPage from './pages/Board';
import MyPage from './pages/MyPage';
import LogIn from './pages/Auth/LogIn';
import UserRegister from './pages/Auth/Register/UserRegister';
import InfoSidebar from './components/Sidebar/InfoSidebar'
import RankSystemSidebar from './components/Sidebar/RankSystemSidebar'
import './styles/base.css'


function NavBar({ isLoggedIn }) {
  const [anchorEl, setAnchorEl] = useState(null);

  const handleOpen = (event) => {
    setAnchorEl(event.currentTarget);
  };

  const handleClose = () => {
    setAnchorEl(null);
  };
  return (
    <AppBar position="static" className='theme-blueberry' style={{ backgroundColor : 'var(--dark)'}}>
      <Toolbar>
<<<<<<< HEAD
        <Link to="/" style={{ textDecoration: 'none', color: 'inherit', flexGrow: 1 }}>
          <Typography variant="h6" component="div">
            HappyDog!
          </Typography>
        </Link>
=======
        <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
          HappyDog! Test
        </Typography>
>>>>>>> 6cae4396fdb320977443bfdc0588da211803af19
        <Button color="inherit" component={Link} to="/">Home</Button>
        <Button color="inherit" component={Link} to="/shelter">보호소</Button>
        <Button color="inherit" component={Link} to="/adoption">입양하기</Button>
        <Button color="inherit" component={Link} to="/board">게시판</Button>
        {isLoggedIn ? (
          <Button color="inherit" component={Link} to="/mypage">마이페이지</Button>
        ) : (
          <>
            <Button color="inherit" onClick={handleOpen}>
              <AccountCircleIcon /> {/* 사람모양 아이콘 */}
            </Button>
            <Menu
              anchorEl={anchorEl}
              open={Boolean(anchorEl)}
              onClose={handleClose}
            >
              <MenuItem onClick={handleClose} component={Link} to="/login">로그인</MenuItem>
              <MenuItem onClick={handleClose} component={Link} to="/signup">회원가입</MenuItem>
            </Menu>
          </>
        )}
      </Toolbar>
    </AppBar>
  );
}


function App() {
  const location = useLocation();
  const isLoggedIn = useSelector(state => state.user.isLoggedIn);

  const authPage = ['/signup', '/login']
  const pageCheck = authPage.includes(location.pathname)

  const backgroundColor = pageCheck ? 'var(--yellow1)' : 'var(--yellow3)'

  return (
    <>
      <div className="theme-yellow" style={{ height: '100vh', backgroundColor: 'var(--yellow4)' }}>
      <NavBar isLoggedIn={isLoggedIn}/>

        <Grid container spacing={3} style={{ height: 'calc(100% - 64px)' }}>
          <Grid item xs={3}> {/* 왼쪽 3칸 */}
            <Box border={1} borderColor="grey.900" height="100%">
              <Grid container direction="column" style={{ height: '100%' }}>
                <Grid item style={{ flex: 2 }}>
                  <Box border={1} borderColor="grey.900" height="100%">
                    <InfoSidebar />
                  </Box>
                </Grid>
                <Grid item style={{ flex: 3 }}>
                  <Box border={1} borderColor="grey.900" height="100%" style={{ backgroundColor: 'var(--yellow6)'}}>
                    <RankSystemSidebar />
                  </Box>
                </Grid>
              </Grid>
            </Box>
          </Grid>

          <Grid item xs={9}> {/* 오른쪽 9칸 */}
            <Box border={1} borderColor="grey.900" height="100%" style={{ backgroundColor }}>
              <Routes>
                <Route path="/" exact element={<MainPage />} />
                <Route path="/shelter" element={<ShelterPage />} />
                <Route path="/adoption" element={<AdoptionPage />} />
                <Route path="/board" element={<BoardPage />} />
                <Route path="/mypage" element={<MyPage />} />
                <Route path="/login" element={<LogIn />} />
                <Route path="/signup" element={<UserRegister />} />
              </Routes>
            </Box>
          </Grid>
        </Grid>
      </div>
    </>
  );
}

export default function WrappedApp() {
  return (
    <Provider store={store}>
      <Router>
        <App />
      </Router>
    </Provider>
  )
}
