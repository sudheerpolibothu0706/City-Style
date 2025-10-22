import React from 'react'
import { assets } from '../assets/assets'
import { NavLink,Link} from 'react-router-dom';

const Footer = () => {
  return (
    <div className='my-10 mt-40'> 
      <div className="flex flex-col sm:grid grid-cols-[3fr_1fr_1fr] gap-4 my-10 tex-sm">
        <div>
          <img src={assets.CityStylelogo} alt="Logo" className='mb-5 w-32' />
          <p className='w-full md:w-2/3 text-gray-600'>
           The name City Style is synonymous with clothing, jackets and accessories. 
            In short span, we have become the destination of choice for young fashion-conscious 
            women and men who want good quality, trendy, affordable Products that are as chic as their individual sense of style. 
            Catering to a range of occasions, our collection includes denim, shirts, jackets, and more
          </p>
        </div>
        <div>
          <p className='text-xl font-medium mb-5'>COMPANY</p>
          <ul className='flex flex-col gap-1 text-gray-600 '>
            <Link to='/'><p>Home</p></Link>
            <Link to='/aboutus'>About Us</Link>
            <Link to='/policy'>Delivery & Return Policy</Link>
            <Link>Privacy Policy</Link>
          </ul>
        </div>
        <div>
          <p className='text-xl font-medium mb-5'>GET IN TOUCH</p>
          <ul className="flex flex-col gap-1 text-gray-600 ">
            <li>sudheerpolibothu@gmail.com</li>
            <li>+91 9703202825</li>
          </ul>
        </div>
      </div>
    
      <div className='mt-8'> 
        <hr />
        <p className='py-5 text-sm text-center'>@Copyrights 2025 SudheerPolibothu - All Rights Reserved</p>
      </div>
    </div>
  )
}

export default Footer