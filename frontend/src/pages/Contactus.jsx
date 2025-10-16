import React from "react";
import Title from "./../components/Title";
import { assets } from "../assets/assets";
import Subscribe from "../components/Subscribe";

function Contact() {
  return (
    <div>
      <div className="text-center text-2xl pt-10 border-t">
        <Title text1={"CONTACT"} text2={"US"} />
      </div>
      <div className="my-10 flex flex-col justify-start md:flex-row gap-10 mb-28">
        <img
          src={assets.contact_img}
          alt="contact_img"
          className="w-full md:max-w-[480px]"
        />
        <div className="flex flex-col justify-center items-start gap-6">
          <p className="font-semibold text-xl text-gray-600">Our Store</p>
          <p className="text-gray-500 ">
            Main Road Pathapatnam <br />
            Srikakulam, A.P
          </p>
          <p className="text-gray-500 ">
            Tel : 9703202825
            <br />
            Email : sudheerpolibothu@gmail.com
          </p>
          
        </div>
      </div>
      <Subscribe/>
    </div>
  );
}

export default Contact;