import React, { useEffect, useState } from "react";

const Loader = () => {
  const [visible, setVisible] = useState(false);

  useEffect(() => {
    const timer = setTimeout(() => setVisible(true), 10);

    document.body.style.overflow = "hidden";

    return () => {
      clearTimeout(timer);
      document.body.style.overflow = "auto"; 
    };
  }, []);

  return (
    <div
      className={`fixed inset-0 z-50 flex items-center justify-center bg-black/30 backdrop-blur-sm
      transition-opacity duration-300 ease-out
      ${visible ? "opacity-100" : "opacity-0"}
      pointer-events-auto`}
      style={{ pointerEvents: "auto" }} 
    >
      <div className="relative flex items-center justify-center pointer-events-none">
        <div className="w-28 h-28 border-4 border-gray-300 border-t-black rounded-full animate-spin"></div>

        <img
          src="https://res.cloudinary.com/dg3lkz3jn/image/upload/v1761993685/city-style/products/ucsfxl05zpngx0ys8m07.png"
          alt="App Icon"
          className="w-14 h-14 absolute rounded-full"
        />
      </div>
    </div>
  );
};

export default Loader;
