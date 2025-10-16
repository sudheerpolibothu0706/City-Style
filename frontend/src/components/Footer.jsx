import React from 'react'
import { assets } from '../assets/assets'

const Footer = () => {
  return (
    // Outer div for padding and margin. Adjusted to match original's mobile/desktop margin.
    <div className='my-10 mt-40'> 
      
      {/* Main content grid: flex-col on mobile, then a 3-column grid on small screens (sm) and up */}
      {/* The grid layout is 3fr, 1fr, 1fr for the logo/text, company links, and contact info, which is the key to the responsiveness */}
      <div className="flex flex-col sm:grid grid-cols-[3fr_1fr_1fr] gap-4 my-10 tex-sm">
        
        {/* Logo and Description Section (3fr column) */}
        <div>
          {/* Logo styling from the original: w-32 and mb-5 */}
          <img src={assets.CityStylelogo} alt="Logo" className='mb-5 w-32' />
          {/* Description text styling: full width on mobile, 2/3 width on medium screens (md) */}
          <p className='w-full md:w-2/3 text-gray-600'>
            Lorem ipsum dolor sit amet consectetur adipisicing elit. Ratione quis nobis molestias culpa nemo eius fugit esse sequi error. Molestias similique facere quae aliquam! Nisi modi distinctio hic pariatur minus.
          </p>
        </div>

        {/* Company Links Section (1fr column) */}
        <div>
          {/* Title styling from the original: text-xl, font-medium, mb-5 */}
          <p className='text-xl font-medium mb-5'>COMPANY</p>
          {/* List styling: flex-col with gap-1, text-gray-600 */}
          <ul className='flex flex-col gap-1 text-gray-600 '>
            <li>Home</li>
            <li>About Us</li>
            <li>Delivery</li>
            <li>Privacy Policy</li>
          </ul>
        </div>

        {/* Get In Touch Section (1fr column) */}
        <div>
          {/* Title styling from the original: text-xl, font-medium, mb-5 */}
          <p className='text-xl font-medium mb-5'>GET IN TOUCH</p>
          {/* Using a ul for consistency, or just keep as paragraphs. Changed to ul/li to match the original structure's list appearance.*/}
          <ul className="flex flex-col gap-1 text-gray-600 ">
            <li>sudheerpolibothu@gmail.com</li>
            <li>+91 9703202825</li>
          </ul>
        </div>
      </div>
      
      {/* Footer bottom section: Divider and Copyright */}
      <div className='mt-8'> {/* Added some top margin for spacing from the main content */}
        <hr />
        {/* Copyright text styling: py-5, text-sm, text-center */}
        <p className='py-5 text-sm text-center'>@Copyrights 2025 Sudheerpolibothu - All Rights Reserved</p>
      </div>
    </div>
  )
}

export default Footer