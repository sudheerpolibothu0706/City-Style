import React from "react";
import { Link } from "react-router-dom";
import { CheckCircle } from "lucide-react";  

const Sucess = () => {
  return (
    <div className="flex flex-col items-center justify-center min-50%-screen p-8">
      <CheckCircle size={80} className="text-green-600 mb-4" />
      <h1 className="text-3xl font-bold text-green-700 mb-2">
        Payment Successful!
      </h1>
      <p className="text-gray-700 mb-6">
        Thank you for your purchase. Your order has been confirmed.
      </p>
      <Link
        to="/"
        className="bg-green-600 text-white px-6 py-2 rounded-lg hover:bg-green-700 transition"
      >
        Go to Home
      </Link>
    </div>
  );
};

export default Sucess;
