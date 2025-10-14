import React from 'react'
import { assets } from '../assets/assets'

const OurPolicy = () => {
  return (
    <div className='flex flex-row justify-around mt-20 text-center'>
      <div>
        <img src={assets.exchange_icon} className='w-12 m-auto mb-5'></img>
        <p className='font-semibold'>Easy Exchange Policy</p>
        <p className='text-gray-400'>We Offer free exchange policy</p>
      </div>

      <div>
        <img src={assets.quality_icon} className='w-12 m-auto mb-5'></img>
        <p className='font-semibold'>7days Return Policy</p>
        <p className='text-gray-400'>We Offer 7days return policy</p>
      </div>

      <div>
        <img src={assets.support_img} className='w-12 m-auto mb-5'></img>
        <p className='font-semibold'>Best Customer Support </p>
        <p className='text-gray-400'>We provide 24/7 customer support</p>
      </div>
    </div>
  )
}

export default OurPolicy
