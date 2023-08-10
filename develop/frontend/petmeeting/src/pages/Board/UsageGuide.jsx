import { Box, Typography } from "@mui/material";
import React from "react";
import UsageGuideMain from "../../components/Board/UsageGuideMain";
// import { UseSelector } from "react-redux";

function UsageGuide() {
    return (
        <Box>
            <Typography>
                이용방법
            </Typography>
            <UsageGuideMain />
        </Box>
    )
}

export default UsageGuide;