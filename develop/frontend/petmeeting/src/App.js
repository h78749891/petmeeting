import React, { useState, useEffect } from "react";
import {
  BrowserRouter as Router,
  Route,
  Routes,
  Link,
  useLocation,
} from "react-router-dom";
import { Provider, useSelector, useDispatch } from "react-redux";
import store from "./stores/index";
import { login } from "./stores/Slices/UserSlice";
import {
  AppBar,
  Button,
  Typography,
  Grid,
  Box,
  Menu,
  MenuItem,
  Hidden,
  Toolbar,
} from "@mui/material";
import AccountCircleIcon from "@mui/icons-material/AccountCircle";
import MainPage from "./pages/MainPage";
import ShelterPage from "./pages/Shelter";
import ShelterDetailPage from "./pages/ShelterDetail";
import AdoptionPage from "./pages/Adoption";
import BoardPage from "./pages/Board";
import MyPage from "./pages/MyPage";
import ShelterMyPage from "./pages/MyPage/ShelterMyPage/ShelterMyPage";
import LogIn from "./pages/Auth/LogIn";
import UserRegister from "./pages/Auth/Register/UserRegister";
import InfoSidebar from "./components/Sidebar/InfoSidebar";
import RankSystemSidebar from "./components/Sidebar/RankSystemSidebar";
import BroadCastingPage from "./pages/BroadCasting";
import "./styles/base.css";

function NavBar({ isLoggedIn }) {
  const [anchorEl, setAnchorEl] = useState(null);

  const handleOpen = (event) => {
    setAnchorEl(event.currentTarget);
  };

  const user = JSON.parse(localStorage.getItem("user"));
  const userType = user ? user.usertype : null;
  const handleClose = () => {
    setAnchorEl(null);
  };
  return (
    <AppBar
      position="static"
      className="theme-blueberry"
      style={{ backgroundColor: "var(--dark)" }}
    >
      <Toolbar>
        <Link
          to="/"
          style={{ textDecoration: "none", color: "inherit", flexGrow: 1 }}
        >
          <Typography variant="h6" component="div">
            HappyDog!
          </Typography>
        </Link>
        <Button color="inherit" component={Link} to="/">
          Home
        </Button>
        <Button color="inherit" component={Link} to="/shelter">
          보호소
        </Button>
        <Button color="inherit" component={Link} to="/adoption">
          입양하기
        </Button>
        <Button color="inherit" component={Link} to="/board">
          게시판
        </Button>
        {isLoggedIn ? (
          <>
            {userType === "보호소" ? (
              <Button
                color="inherit"
                component={Link}
                to="/mypage/ShelterMyPage"
              >
                보호소 마이페이지
              </Button>
            ) : (
              <Button color="inherit" component={Link} to="/mypage">
                마이페이지
              </Button>
            )}
          </>
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
              <MenuItem onClick={handleClose} component={Link} to="/login">
                로그인
              </MenuItem>
              <MenuItem onClick={handleClose} component={Link} to="/signup">
                회원가입
              </MenuItem>
            </Menu>
          </>
        )}
      </Toolbar>
    </AppBar>
  );
}

function App() {
  const location = useLocation();
  const dispatch = useDispatch();
  const isLoggedIn = useSelector((state) => state.user.isLoggedIn);

  useEffect(() => {
    const token = sessionStorage.getItem("token");

    if (token) {
      const user = localStorage.getItem("user");
      const userData = JSON.parse(user);
      dispatch(login({ userId: userData.name }));
    }
  }, [dispatch]);

  const authPage = ["/signup", "/login"];
  const pageCheck = authPage.includes(location.pathname);

  const backgroundColor = pageCheck ? "var(--yellow1)" : "var(--yellow2)";

  return (
    <>
      <div
        className="theme-yellow"
        style={{
          minHeight: "120vh",
          height: "100%",
          backgroundColor: "var(--yellow3)",
          overflowYL: "auto",
        }}
      >
        <NavBar isLoggedIn={isLoggedIn} />

        <Grid container spacing={3} style={{ height: "calc(100% - 64px)" }}>
          <Hidden smDown>
            {/* 로그인 또는 회원가입 페이지가 아니면 왼쪽 영역을 표시 */}
            {!pageCheck && (
              <Grid
                item
                xs={3}
                style={{ maxHeight: "calc(100vh - 64px)", borderRadius: "8px" }}
              >
                {" "}
                {/* 왼쪽 3칸 */}
                <Box border={1} borderColor="grey.900" height="100%">
                  <Grid container direction="row" style={{ height: "100%" }}>
                    <Grid item style={{ flex: 2 }}>
                      <Box
                        border={1}
                        borderColor="grey.900"
                        height="100%"
                        borderRadius="8px"
                      >
                        <InfoSidebar />
                      </Box>
                    </Grid>
                    <Grid item style={{ flex: 3 }} sx={{ mt: 2 }}>
                      <Box
                        border={1}
                        borderColor="grey.900"
                        height="100%"
                        style={{
                          backgroundColor: "var(--yellow6)",
                          borderRadius: "8px",
                        }}
                      >
                        <RankSystemSidebar />
                      </Box>
                    </Grid>
                  </Grid>
                </Box>
              </Grid>
            )}
          </Hidden>

          <Grid item xs={pageCheck ? 12 : 9}>
            {" "}
            {/* 로그인 또는 회원가입 페이지이면 전체 영역, 아니면 오른쪽 9칸 */}
            <Box
              border={1}
              borderColor="grey.900"
              minHeight="85vh"
              height="100%"
              style={{ backgroundColor }}
            >
              <Routes>
                <Route path="/" exact element={<MainPage />} />
                <Route path="/shelter" element={<ShelterPage />} />
                <Route
                  path="/shelter/:shelterNo"
                  element={<ShelterDetailPage />}
                />
                <Route path="/adoption" element={<AdoptionPage />} />
                <Route path="/board" element={<BoardPage />} />
                <Route path="/mypage" element={<MyPage />} />
                <Route
                  path="/Mypage/ShelterMyPage"
                  element={<ShelterMyPage />}
                />
                <Route path="/login" element={<LogIn />} />
                <Route path="/signup" element={<UserRegister />} />
                <Route
                  path="/broadcasting/:broadcastId"
                  element={<BroadCastingPage />}
                ></Route>
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
  );
}
