import React, { useState } from "react";

function InquiryList(props) {
    const [hoverd, setHovered] = useState(false);


    return (
        <div 
            onMouseEnter={() => setHovered(true)}
            onMouseLeave={() => setHovered(false)}
        >
            <div>{props.inquiry.title}</div>
            {/*props.board.title*/}
        </div>
    )
}

export default InquiryList;