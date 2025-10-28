import React, { useContext } from "react";
import { shopContext } from "../context/ShopContext";
import { Link } from "react-router-dom";

function ProductItem({ id, imageUrls, name, price }) {
  const { currency } = useContext(shopContext);

  const imageSrc =
    Array.isArray(imageUrls) && imageUrls.length > 0
      ? imageUrls[0]
      : "https://via.placeholder.com/150";

  return (
    <Link className="text-gray-700 cursor-pointer" to={`/product/${id}`}>
      <div className="overflow-hidden border rounded-md">
        <img
          src={imageSrc}
          alt={name}
          className="hover:scale-110 transition ease-in-out duration-500 border rounded-md"
        />
      </div>
      <p className="pt-3 pb-1 text-sm">{name}</p>
      <p className="text-sm font-medium">
        {currency}
        {price}
      </p>
    </Link>
  );
}

export default ProductItem;
