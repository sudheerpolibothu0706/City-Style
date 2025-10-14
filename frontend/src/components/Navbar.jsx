import React, { useState, useContext } from 'react';
import { assets } from '../assets/assets';
import { Link, NavLink, useNavigate } from 'react-router-dom';
import { shopContext } from "../context/ShopContext";

const Navbar = () => {
  const [menuOpen, setMenuOpen] = useState(false);
  const [showDropdown, setShowDropdown] = useState(false);
  const { getCartCount,setToken, token,navigate,setCartItems } = useContext(shopContext);
  

  const handleLogout = () => {
    localStorage.removeItem("token");
    setToken("");
    setCartItems({});
    navigate("/login");
  };

  
  const handleProfileClick = () => {
    if (!token) {
      navigate('/login');
    } else {
      setShowDropdown((prev) => !prev); 
    }
  };

  return (
    <div className="relative">
      
      <div className="flex justify-between items-center py-5 px-5 sm:px-32 font-medium">
        
        <Link to={'/'}>
          <img src={assets.CityStylelogo} className='w-[166px] h-[58px]' alt="Logo" />
        </Link>

        
        <ul className='hidden sm:flex flex-row gap-5 text-sm'>
          <NavLink to='/' className='flex flex-col items-center text-gray-700 hover:text-black'>
            <p>HOME</p>
            <hr className="w-3/4 mt-1 border-none h-[1.5px] bg-gray-700 hidden" />
          </NavLink>
          <NavLink to='/collection' className='flex flex-col items-center text-gray-700 hover:text-black'>
            <p>COLLECTION</p>
            <hr className="w-3/4 mt-1 border-none h-[1.5px] bg-gray-700 hidden" />
          </NavLink>
          <NavLink to='/aboutus' className='flex flex-col items-center text-gray-700 hover:text-black'>
            <p>ABOUT US</p>
            <hr className="w-3/4 mt-1 border-none h-[1.5px] bg-gray-700 hidden" />
          </NavLink>
          <NavLink to='/contactus' className='flex flex-col items-center text-gray-700 hover:text-black'>
            <p>CONTACT US</p>
            <hr className="w-3/4 mt-1 border-none h-[1.5px] bg-gray-700 hidden" />
          </NavLink>
        </ul>

        
        <div className='flex flex-row gap-5 items-center'>
          {/* Search */}
          <img src={assets.search_icon} className='w-5 h-5 cursor-pointer' alt="Search" />

          
          <div className="relative">
            <img
              src={assets.profile_icon}
              alt="Profile"
              className="w-5 h-5 cursor-pointer"
              onClick={handleProfileClick}
            />

            
            {token && showDropdown && (
              <div
                className="absolute right-0 mt-2 w-36 py-3 px-5 bg-slate-100 text-gray-500 rounded shadow-lg transition-all duration-200 z-20"
              >
                <p
                  className="cursor-pointer hover:text-black capitalize"
                  onClick={() => {
                    navigate("/profile");
                    setShowDropdown(false);
                  }}
                >
                  My Profile
                </p>
                <p
                  className="cursor-pointer hover:text-black capitalize"
                  onClick={() => {
                    navigate("/myorders");
                    setShowDropdown(false);
                  }}
                >
                  Orders
                </p>
                <p
                  className="cursor-pointer hover:text-black capitalize"
                  onClick={handleLogout}
                >
                  Logout
                </p>
              </div>
            )}
          </div>

          
          <div className='relative'>
            <NavLink to='/cart'>
              <img src={assets.cart_icon} className='w-5 h-5 cursor-pointer' alt="Cart" />
              <span className='text-xs absolute -bottom-2 -right-1 rounded-full bg-black text-white px-1'>
                {getCartCount()}
              </span>
            </NavLink>
          </div>

          
          <img
            src={assets.menu_icon}
            onClick={() => setMenuOpen(true)}
            className='block sm:hidden w-7 h-5 cursor-pointer'
            alt="Menu"
          />
        </div>
      </div>

      {/* ---------------- Mobile Menu ---------------- */}
      <div
        className={`fixed top-0 right-0 w-full h-screen bg-white flex flex-col z-50 transform transition-transform duration-300 ease-in-out ${
          menuOpen ? 'translate-x-0' : 'translate-x-full'
        }`}
      >
        <img
          src={assets.cross_icon}
          onClick={() => setMenuOpen(false)}
          className='absolute top-8 left-4 w-6 h-6 cursor-pointer'
          alt="Close"
        />

        <ul className='flex flex-col gap-4 mt-20 w-full text-center'>
          <NavLink to='/' onClick={() => setMenuOpen(false)} className='text-gray-600 text-xl py-2 hover:bg-gray-200'>HOME</NavLink>
          <NavLink to='/collection' onClick={() => setMenuOpen(false)} className='text-gray-600 text-xl py-2 hover:bg-gray-200'>COLLECTION</NavLink>
          <NavLink to='/aboutus' onClick={() => setMenuOpen(false)} className='text-gray-600 text-xl py-2 hover:bg-gray-200'>ABOUT US</NavLink>
          <NavLink to='/contactus' onClick={() => setMenuOpen(false)} className='text-gray-600 text-xl py-2 hover:bg-gray-200'>CONTACT US</NavLink>

          {token ? (
            <>
              <p className='text-gray-600 text-xl py-2 cursor-pointer hover:bg-gray-200' onClick={() => { navigate("/profile"); setMenuOpen(false); }}>My Profile</p>
              <p className='text-gray-600 text-xl py-2 cursor-pointer hover:bg-gray-200' onClick={() => { navigate("/myorders"); setMenuOpen(false); }}>Orders</p>
              <p className='text-gray-600 text-xl py-2 cursor-pointer hover:bg-gray-200' onClick={handleLogout}>Logout</p>
            </>
          ) : (
            <NavLink to='/login' onClick={() => setMenuOpen(false)} className='text-gray-600 text-xl py-2 hover:bg-gray-200'>Login</NavLink>
          )}
        </ul>
      </div>
    </div>
  );
};

export default Navbar;
