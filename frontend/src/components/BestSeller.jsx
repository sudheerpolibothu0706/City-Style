import React, { useContext, useEffect, useState } from "react";
import Title from "./Title";
import ProductItem from "./ProductItem";
import { shopContext, } from "../context/ShopContext";

const BestSeller = () => {
  const { products } = useContext(shopContext);
  const [bestSeller, setBestSeller] = useState([]);

  useEffect(() => {
    
    const filteredProducts = products.filter((product) => product.bestseller);
    setBestSeller(filteredProducts.slice(0, 5)); 
  }, [products]);

  return (
    <div>
      <div className="text-center py-8 text-3xl">
        <Title text1={"BEST"} text2={"SELLER"} />
        <p className="w-3/4 m-auto text-xs sm:text-sm md:text-base text-gray-700 font-semibold">
          Discover our best sellers – the pieces everyone is talking about! Loved for their style, quality, 
          and comfort, these wardrobe favorites are tried, tested, and adored by our customers. From timeless classics to trend-setting designs, our best-selling collection is your shortcut to effortless style and confidence every day.
        </p>
      </div>
      <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5 gap-4  gap-y-6 pl-14 pr-14">
        {bestSeller.map((item) => (
          <ProductItem
            key={item._id}
            id={item._id}
            image={item.image}
            name={item.name}
            price={item.price}
          />
        ))}
      </div>
    </div>
  );
};

export default BestSeller;
