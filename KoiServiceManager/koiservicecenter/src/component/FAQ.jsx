import './css/FAQ.css';
import { useState } from 'react';
function FAQ() {
    const faqData = [
        {
          question: "What services does the koi care center provide?",
          answer: "We provide pond cleaning, fish health checks, feeding guidance, and water quality assessments.",
        },
        {
          question: "How often should I have my pond cleaned?",
          answer: "We recommend cleaning every 4-6 months, depending on pond size and fish population.",
        },
        {
          question: "What is the process for booking a service?",
          answer: "You can book a service through our website, call us, or visit our center for in-person appointments.",
        },
        {
          question: "Do you offer emergency services for koi health issues?",
          answer: "Yes, we offer emergency services for koi fish health issues. Please contact our hotline immediately if you notice signs of illness.",
        },
      ];
    
      const [activeIndex, setActiveIndex] = useState(null);
    
      const toggleAnswer = (index) => {
        setActiveIndex(activeIndex === index ? null : index);
      };
    
      return (
        <div className="faq">
          <h2>Frequently Asked Questions</h2>
          <ul>
            {faqData.map((item, index) => (
              <li key={index} className={`faq-item ${activeIndex === index ? 'active' : ''}`}>
                <button onClick={() => toggleAnswer(index)} className="faq-question">
                  {item.question}
                </button>
                {activeIndex === index && (
                  <p className="faq-answer">
                    {item.answer}
                  </p>
                )}
              </li>
            ))}
          </ul>
        </div>
      );
}

export default FAQ;
