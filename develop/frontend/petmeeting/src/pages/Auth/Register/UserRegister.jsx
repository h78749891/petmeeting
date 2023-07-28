import * as React from "react";
import axios from "axios";
import Select from "@mui/material/Select";
import MenuItem from "@mui/material/MenuItem";
import FormControl from "@mui/material/FormControl";
import InputLabel from "@mui/material/InputLabel";
import Avatar from "@mui/material/Avatar";
import Button from "@mui/material/Button";
import CssBaseline from "@mui/material/CssBaseline";
import TextField from "@mui/material/TextField";
import FormControlLabel from "@mui/material/FormControlLabel";
import Checkbox from "@mui/material/Checkbox";
import Link from "@mui/material/Link";
import Grid from "@mui/material/Grid";
import Box from "@mui/material/Box";
import LockOutlinedIcon from "@mui/icons-material/LockOutlined";
import Typography from "@mui/material/Typography";
import Container from "@mui/material/Container";
import { createTheme, ThemeProvider } from "@mui/material/styles";
import "../../../styles/base.css";
import {
  setPassword as setReduxPassword,
  login,
} from "../../../stores/Slices/UserSlice";
import { useNavigate } from "react-router";

import { useDispatch, useSelector } from "react-redux";

function Copyright(props) {
  return (
    <Typography
      variant="body2"
      color="text.secondary"
      align="center"
      {...props}
    >
      {"Copyright © "}
      <Link color="inherit" href="https://mui.com/">
        Your Website
      </Link>{" "}
      {new Date().getFullYear()}
      {"."}
    </Typography>
  );
}

const defaultTheme = createTheme();

export default function SignUp() {
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const handleSubmit = async (event) => {
    event.preventDefault();
    const data = new FormData(event.currentTarget);

    try {
      console.log(data.get("userId"));
      const response = await axios({
        method: "post",
        url: "http://i9a203.p.ssafy.io/backapi/api/v1/user/sign-up",
        data: {
          userId: data.get("userId"),
          password: data.get("password"),
          name: data.get("name"),
          phoneNumber: data.get("phoneNumber"),
          userGroup: data.get("userGroup"),
          //   userGroup: "사용자",
          imagePath: data.get("imagePath"),
        },
      });

      console.log("Signup successful:", response.data);
      if (response.status === 201) {
        console.log("ㅡㄷㄹ어왔음");
        dispatch(
          login({
            userId: data.get("name"),
            points: response.data.points,
          })
        ); // 로그인 상태로 설정
        navigate("/"); // Home으로 이동
      } else {
        console.log("Login failed");
      }
    } catch (error) {
      console.error("Signup failed:", error);
    }
  };
  return (
    <ThemeProvider theme={defaultTheme}>
      <Container component="main" maxWidth="xs">
        <CssBaseline />
        <Box
          sx={{
            marginTop: 8,
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
          }}
        >
          <Avatar sx={{ m: 1, bgcolor: "secondary.main" }}>
            <LockOutlinedIcon />
          </Avatar>
          <Typography component="h1" variant="h5">
            Sign up
          </Typography>
          <Box
            component="form"
            noValidate
            onSubmit={handleSubmit}
            sx={{ mt: 3 }}
          >
            <Grid container spacing={2}>
              <Grid item xs={12}>
                <TextField
                  required
                  fullWidth
                  id="userId"
                  label="User ID"
                  name="userId"
                  autoComplete="username"
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  required
                  fullWidth
                  name="password"
                  label="Password"
                  type="password"
                  id="password"
                  autoComplete="new-password"
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  required
                  fullWidth
                  id="name"
                  label="Name"
                  name="name"
                  autoComplete="name"
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  required
                  fullWidth
                  id="phoneNumber"
                  label="Phone Number"
                  name="phoneNumber"
                />
              </Grid>
              <Grid item xs={12}>
                <FormControl fullWidth required>
                  <InputLabel id="userGroup-label">회원 유형</InputLabel>
                  <Select
                    labelId="userGroup-label"
                    id="userGroup"
                    name="userGroup"
                    label="userGroup"
                    defaultValue=""
                  >
                    <MenuItem value={"사용자"}>사용자</MenuItem>
                    <MenuItem value={"보호소"}>보호소</MenuItem>
                  </Select>
                </FormControl>
              </Grid>

              {/* <Grid item xs={12}>
                <TextField
                  required
                  fullWidth
                  id="userGroup"
                  label="User Group"
                  name="userGroup"
                />
              </Grid> */}
              <Grid item xs={12}>
                <TextField
                  required
                  fullWidth
                  id="imagePath"
                  label="Image Path"
                  name="imagePath"
                />
              </Grid>

              <Grid item xs={12}>
                <FormControlLabel
                  control={
                    <Checkbox value="allowExtraEmails" color="primary" />
                  }
                  label="I want to receive inspiration, marketing promotions and updates via email."
                />
              </Grid>
            </Grid>
            <Button
              type="submit"
              fullWidth
              variant="contained"
              sx={{ mt: 3, mb: 2 }}
            >
              Sign Up
            </Button>
            <Grid container justifyContent="flex-end">
              <Grid item>
                <Link href="#" variant="body2">
                  Already have an account? Sign in
                </Link>
              </Grid>
            </Grid>
          </Box>
        </Box>
        <Copyright sx={{ mt: 5 }} />
      </Container>
    </ThemeProvider>
  );
}
