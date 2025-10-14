import React, { useContext, useEffect, useState } from 'react'
import { shopContext,  } from '../context/ShopContext'
import ProductItem from './ProductItem';

const CollectionProducts = () => {
    const { products } = useContext(shopContext);
    const [collection,setCollection]= useState([]);

    useEffect(() => {
      
      setCollection(products); 
    }, []);
    
  return (
    <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5 gap-4  gap-y-6 pl-14 pr-14">
        {
            collection.map( (item,index)=>(
               <ProductItem key={index} id={item._id} image={item.image} name={item.name} price={item.price}/>
            )

            )
        }
        
      
    </div>
  )
}

export default CollectionProducts
