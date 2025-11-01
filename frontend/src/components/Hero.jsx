import React from 'react'
import { assets } from '../assets/assets';

const Hero = () => {
  return (
    <div className="flex flex-col sm:flex-row h-screen sm:h-[70vh] border border-gray-400 overflow-hidden">
      
      {/* Left side */}
      <div className="flex justify-center items-center flex-1 py-10 sm:py-0">
        <div className="text-[#414141] px-6 sm:px-10">
          <div className="flex items-center gap-2">
            <p className="w-8 md:w-11 h-[2px] bg-[#414141]"></p>
            <p className="font-medium text-sm md:text-base uppercase">
              OUR BESTSELLERS
            </p>
          </div>

          <h1 className="capitalize text-3xl sm:py-3 lg:text-5xl leading-relaxed">
            Latest Arrivals
          </h1>

          <div className="flex items-center gap-2">
            <p className="uppercase text-sm md:text-base font-semibold">
              SHOP NOW
            </p>
            <p className="w-8 md:w-11 h-[1px] bg-[#414141]"></p>
          </div>
        </div>
      </div>

      {/* Right side */}
      <div className="flex-1 h-1/2 sm:h-full">
        <img
          src="https://res.cloudinary.com/dg3lkz3jn/image/upload/v1761995348/city-style/products/skdf3ktn6l6mcfsy3xs8.jpg"
          alt="hero_img"
          className="w-full h-full object-cover object-center"
        />
      </div>

    </div>
  )
}

export default Hero
