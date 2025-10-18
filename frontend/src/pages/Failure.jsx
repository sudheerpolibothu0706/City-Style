import React from 'react'
import { XCircle } from 'lucide-react'        
import { Link } from 'react-router-dom' 

const Failure = () => {
  return (
    <div className="flex flex-col items-center justify-center min-50%-screen p-8">
      <XCircle size={80} className="text-red-600 mb-4" />
      <h1 className="text-3xl font-bold text-red-700 mb-2">Payment Failed</h1>
      <p className="text-gray-700 mb-6">Your payment was not completed. Please try again.</p>
      <Link
        to="/cart"
        className="bg-red-600 text-white px-6 py-2 rounded-lg hover:bg-red-700 transition"
      >
        Go Back to Cart
      </Link>
    </div>
  )
}

export default Failure
