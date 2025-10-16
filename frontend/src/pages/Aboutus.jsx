import React from "react";
import Title from "../components/Title";
import { assets } from "../assets/assets";
import Subscribe from "../components/Subscribe";

function About() {
  return (
    <div>
      <div className="text-2xl text-center pt-8 border-t ">
        <Title text1={"ABOUT"} text2={"US"} />
      </div>
      <div className="my-10 flex flex-col md:flex-row gap-16">
        <img
          src={assets.about_img}
          alt="about_img"
          className="w-full md:max-w-[450px]"
        />
         <div className="flex flex-col border-none justify-center gap-6 md:w-2/4 text-gray-600">
          <p>
            The name City Style is synonymous with clothing, shoes, jackets, and accessories. 
            In short span, we have become the destination of choice for young fashion-conscious 
            women and men who want good quality, trendy, affordable Products that are as chic as their individual sense of style. 
            Catering to a range of occasions, our collection includes denim, shirts, jackets, and more.
          </p>
          <p>
            Apart from the sheer range of products that we carry, our customers love that our catalogue
             is based on a deep understanding of our clientele and what looks good on them. We have 
             successfully served our customers in the offline market for the past 10 years, and are excited to bring that expertise online.
          </p>
          
        </div>
      </div>
      <div className="text-xl py-4">
        <Title text1={"WHY"} text2={"CHOOSE US"} />
      </div>
      <div className="flex flex-col md:flex-row text-sm mb-20">
        <div className="border px-10 md:px-16 py-8 sm:py-20 flex flex-col gap-5">
          <b>Quality Assurance:</b>
          <p className="text-gray-600">
            The quality begins with the material. Feel the luxurious texture of fabrics chosen for both comfort and resilience.
            We choose only the most durable fabrics and secure stitching,
             ensuring your favorites stay your favorites for years
          </p>
        </div>
        <div className="border px-10 md:px-16 py-8 sm:py-20 flex flex-col gap-5">
          <b>Convenience:</b>
          <p className="text-gray-600">
            Convenience in our products means they are easy to use, wear, and care for, 
            seamlessly fitting into your busy lifestyle. They are designed to save you time and effort, making your daily routine simpler and more comfortable.
          </p>
        </div>
        <div className="border px-10 md:px-16 py-8 sm:py-20 flex flex-col gap-5">
          <b> Customer Service:</b>
          <p className="text-gray-600">
            Our dedicated support team is available to assist you quickly and effectively,
             ensuring a seamless and satisfactory experience from browsing to post-purchase.
          </p>
        </div>
      </div>
      <Subscribe/>
    </div>
  );
}

export default About;