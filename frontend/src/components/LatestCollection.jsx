import React, { useContext, useEffect, useState } from "react";
import { shopContext, } from "../context/ShopContext";
import Title from "./Title";
import ProductItem from "./ProductItem";

const LatestCollection = () => {
  const { products } = useContext(shopContext);
  const [latestProducts,setLatestProducts]= useState([]);

  useEffect(()=>{
    setLatestProducts(products.slice(0,10));
  },[])

  return(
    <div className="my-10">
      <div className="text-center py-8 text-3xl">
        <Title text1={'LATEST'} text2={'COLLECTION'}/>
        <p className="w-3/4 m-auto text-xs sm:text-sm md:text-base text-gray-700 font-semibold">
          Upgrade your wardrobe with our latest collection – featuring fresh designs, 
          vibrant colors, and unmatched comfort. From casual everyday wear to statement pieces for special occasions, our new arrivals are crafted to keep you stylish, confident, and ready for every moment. Explore the season's must-have looks and reinvent your personal style today
          </p>
      </div>
        
      <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5 gap-4  gap-y-6 pl-14 pr-14">
      {
        latestProducts.map((item,index)=>(
          <ProductItem key={index} id={item._id} image={item.image} name={item.name} price={item.price}/>
        )
        )
      }
      </div>
    </div>
  )
}

export default LatestCollection;