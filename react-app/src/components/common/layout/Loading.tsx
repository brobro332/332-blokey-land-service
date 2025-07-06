import React, { useEffect, useState } from "react";
import { DotLottieReact } from "@lottiefiles/dotlottie-react";

interface LoadingIndicatorProps {
  loading: boolean;
}

const LoadingIndicator: React.FC<LoadingIndicatorProps> = ({ loading }) => {
  const [show, setShow] = useState(false);

  useEffect(() => {
    setShow(loading);
  }, [loading]);

  if (!show) return null;

  return (
    <div className="fixed inset-0 z-50 flex flex-col justify-center items-center">
      <div className="w-40 h-40">
        <DotLottieReact
          src="https://lottie.host/f5fbbfd2-d2e2-4606-8fbf-1a100bf604eb/NPW5FGCgxb.lottie"
          loop
          autoplay
          style={{ width: "100%", height: "100%" }}
        />
      </div>
    </div>
  );
};

export default LoadingIndicator;
