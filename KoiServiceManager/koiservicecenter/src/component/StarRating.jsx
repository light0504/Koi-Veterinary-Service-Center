import React from 'react';

const StarRating = ({ rating, setRating, label }) => {
    const handleClick = (index) => {
        setRating(index + 1);
    };

    // Inline styles for the rating container and stars
    const ratingContainerStyle = {
        display: 'flex',
        flexDirection: 'row',  // Ensures stars are in a row
        alignItems: 'center',
        justifyContent: 'center',
        marginTop: '10px',
    };

    const starStyle = (index) => ({
        cursor: 'pointer',
        fontSize: '24px', // Adjust the size of the stars
        transition: 'color 0.3s',
        margin: '0 5px', // Space between the stars
        color: index < rating ? 'gold' : 'gray', // Change color based on rating
    });

    const labelStyle = {
        fontWeight: 'bold',
        marginBottom: '5px',
        display: 'block',
        fontSize: '16px',
    };

    return (
        <div>
            <label style={labelStyle}>{label}</label> {/* Display the label here */}
            <div style={ratingContainerStyle}>
                {[...Array(5)].map((star, index) => (
                    <span
                        key={index}
                        onClick={() => handleClick(index)}
                        style={starStyle(index)} // Apply the inline style
                    >
                        ★
                    </span>
                ))}
            </div>
        </div>
    );
};

export default StarRating;
