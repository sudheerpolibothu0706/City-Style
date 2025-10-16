import React from 'react'

const Subscribe = () => {

  const onSubmitHandler = (event) => {
    event.preventDefault();
  }

  return (
    
    <div className='px-4 sm:px-10'> 
      <div className='flex justify-center mt-12 flex-col items-center text-center'> 
        <p className='text-2xl font-bold'>Subscribe now & get 20% off</p>
        
        <p className='text-gray-500 max-w-lg mx-auto'>By subscribing with your email you will get 20% off exclusively</p>

        
        <form 
          onSubmit={onSubmitHandler} 
          className='flex gap-3 mx-auto my-6 pl-3 w-full sm:w-1/2 max-w-xl border text-xs' // Added w-full and max-w-xl
        >
          <input type='email' placeholder='Enter your email' className='flex-1 outline-none' required></input>
          
          <button type='submit' className='bg-black text-white p-3 sm:p-4 text-xs sm:text-sm uppercase'>
            SUBSCRIBE
          </button>
        </form>
        
      </div>
    </div>
  )
}

export default Subscribe