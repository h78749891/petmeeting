import React from 'react';
import { Box, Typography } from '@mui/material';
import BroadCastingView from '../components/BroadCasting/BroadCastingView';
import BroadCastingDetail from '../components/BroadCasting/BroadCastingDetail';
import DeviceOperation from '../components/BroadCasting/DeviceOperation';
import { useSelector } from 'react-redux';
import { useLocation } from 'react-router-dom'

function BroadCasting() {
  const showDevice = useSelector(state => state.device.showDevice)
  const location = useLocation();
  const isLiveSession = location.state?.isLiveSession
  const token = location.state?.token
  const session = location.state?.session


  return (
    <Box sx={{ mt: 2, display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
      <Typography component="h1" variant="h5">
        BroadCasting Page
      </Typography>
      {/* 다른 컨텐츠와 컴포넌트들이 여기에 추가될 수 있습니다. */}
      <BroadCastingView isLiveSession={isLiveSession} token={token} session={session}/>
      {showDevice ? <DeviceOperation /> : <BroadCastingDetail />}
    </Box>
    
  );
}

export default BroadCasting;
