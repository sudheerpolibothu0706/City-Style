import React, { useContext } from "react";
import { shopContext } from "../context/ShopContext";
import Title from "./Title";

const CartTotal = () => {
  const { currency, delivery_fee, getCartAmount } = useContext(shopContext);

  const subtotal = getCartAmount() || 0;
  const total = subtotal + (subtotal > 0 ? delivery_fee : 0);

  return (
    <div className="w-full">
      <Title text1="CART" text2="TOTALS" />
      <div className="flex flex-col gap-2 mt-2 text-sm">
        <div className="flex justify-between">
          <p>Subtotal</p>
          <p>{currency}{subtotal.toFixed(2)}</p>
        </div>
        <hr />
        <div className="flex justify-between">
          <p>Shipping Fee</p>
          <p>{currency}{subtotal > 0 ? delivery_fee.toFixed(2) : "0.00"}</p>
        </div>
        <hr />
        <div className="flex justify-between font-bold">
          <p>Total</p>
          <p>{currency}{total.toFixed(2)}</p>
        </div>
      </div>
    </div>
  );
};

export default CartTotal;
