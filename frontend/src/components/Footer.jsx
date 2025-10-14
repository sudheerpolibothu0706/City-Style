import React from 'react'
import { assets } from '../assets/assets'

const Footer = () => {
  return (
    <div className='mt-25 my-10'>
       <div className='flex flex-row justify-around py-4'>
        <div className='w-1/3'>
        <img src={assets.CityStylelogo} className='w-[166px] h-[128px]' alt="Logo" />
        <p className='w-full text-gray-400'>Lorem ipsum dolor sit amet consectetur adipisicing elit. Ratione quis nobis molestias culpa nemo eius fugit esse sequi error. Molestias similique facere quae aliquam! Nisi modi distinctio hic pariatur minus.</p>
       </div>

       <div>
        <p className='text-2xl font-semibold mt-4'>COMPANY</p>
        <ul className='cursor-pointer text-gray-400 mt-12'>
            <li>Home</li>
            <li>About Us</li>
            <li>Delivery</li>
            <li>Privacy Policy</li>
        </ul>
       </div>

       <div>
        <p className='text-2xl font-semibold mt-4'>GET IN TOUCH</p>
        <p className='text-gray-400 mt-12'>sudheerpolibothu@gmail.com</p>
        <p className='text-gray-400'>+91 9703202825</p>
       </div>
    </div>
        <div >
            <hr></hr>
            <p className='text-center py-4 '>@Copyrights 2025 Sudheerpolibothu - All Rights Reserved</p>
        </div>
    
    </div>
  )
}

export default Footer
