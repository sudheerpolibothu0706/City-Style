import React from 'react'
import { assets } from '../assets/assets';

const Hero = () => {
  return (
    <div className='flex flex-row justify-center border border-gray-400 w-4/5 ml-32'> 

      {/*Left side container*/}
      <div className='flex justify-center items-center w-1/2 py-10'>
            <div className='text-[#414141]'>
            <div className='flex items-center gap-2'>
              <p className="w-11 h-[2px] bg-[#414141]"></p>
              <p className='text-2xl'>OUR BESTSELLERS</p>
            </div>
            <h1 className='py-3'>Latest Arrivals</h1>

            <div className='flex items-center gap-2'>
              <p className='text-2xl'>SHOP NOW</p>
              <p className="w-11 h-[2px] bg-[#414141]"></p>
            </div>
            </div>
       </div>

       {/*Right side container*/}
      <div className='w-1/2 h-96'>
          <img src={assets.hero_img} className='object-cover h-full w-full' ></img>
      </div>
        
      
    </div>
  )
}

export default Hero
