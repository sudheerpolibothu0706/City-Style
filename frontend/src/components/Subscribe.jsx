import React from 'react'

const Subscribe = () => {

  const onSubmitHandler = (event) =>{
    event.preventDefault();
  }

  return (
    <div>
      <div className='flex justify-center mt-12 flex-col items-center'>
        <p className='text-2xl font-bold'>Subscribe now & get 20% off</p>
        <p className='text-gray-500'>By subscribing with your email you will get 20% off exclusively</p>

        <form className='flex gap-3 mx-auto my-6 pl-3 w-1/2 border text-xs'>
        <input type='email' placeholder='Enter your email' className='flex-1 outline-none' required></input>
        <button type='submit' className='bg-black text-white p-4'>SUBSCRIBE</button>
        </form>
        
      </div>
    </div>
  )
}

export default Subscribe
