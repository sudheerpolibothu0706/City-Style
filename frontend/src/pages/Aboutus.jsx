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
        <div className="flex flex-col justify-center gap-6 md:w-2/4 text-gray-600">
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
          <b className="text-gray-800">Our Mission</b>
          <p>
            Our Mission consectetur adipisicing elit. Accusantium dolore
            voluptas doloribus quo atque, reiciendis cum provident dolorum
            quisquam nobis veritatis. Unde ratione, placeat ea eum labore
            distinctio alias illo!
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
            consectetur adipisicing elit. Inventore harum ratione, illo
            temporibus facilis at rem quos magni rerum dolor, totam distinctio
            et, voluptate maxime quo soluta! Ut, numquam quia!
          </p>
        </div>
        <div className="border px-10 md:px-16 py-8 sm:py-20 flex flex-col gap-5">
          <b>Convenience:</b>
          <p className="text-gray-600">
            consectetur adipisicing elit. Inventore harum ratione, illo
            temporibus facilis at rem quos magni rerum dolor, totam distinctio
            et, voluptate maxime quo soluta! Ut, numquam quia!
          </p>
        </div>
        <div className="border px-10 md:px-16 py-8 sm:py-20 flex flex-col gap-5">
          <b>Exceptional Customer Service:</b>
          <p className="text-gray-600">
            consectetur adipisicing elit. Inventore harum ratione, illo
            temporibus facilis at rem quos magni rerum dolor, totam distinctio
            et, voluptate maxime quo soluta! Ut, numquam quia!
          </p>
        </div>
      </div>
      <Subscribe/>
    </div>
  );
}

export default About;