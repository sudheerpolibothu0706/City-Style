import React from 'react'

const Loader = () => {
  return (
   <div className="fixed inset-0 z-50 bg-black/30 backdrop-blur-sm flex items-center justify-center">
      <div className="relative flex items-center justify-center">
       
        <div className="w-28 h-28 border-4 border-gray-300 border-t-black rounded-full animate-spin"></div>

        <img
          src="https://res.cloudinary.com/dg3lkz3jn/image/upload/v1761993685/city-style/products/ucsfxl05zpngx0ys8m07.png"
          alt="App Icon"
          className="w-14 h-14 absolute rounded-full"
        />
      </div>
    </div>
  )
}

export default Loader
