import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import userService from '../services/userService';
import skillService from '../services/skillService';
import feedbackService from '../services/feedbackService';
import { toast } from 'react-toastify';
import { FaStar, FaRegStar } from 'react-icons/fa';

const Profile = () => {
  const { id } = useParams();
  const { user: currentUser } = useAuth();
  const [user, setUser] = useState(null);
  const [offeredSkills, setOfferedSkills] = useState([]);
  const [wantedSkills, setWantedSkills] = useState([]);
  const [feedback, setFeedback] = useState([]);
  const [rating, setRating] = useState(0);
  const [loading, setLoading] = useState(true);
  const [activeTab, setActiveTab] = useState('overview');

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);
        
        // Determine if we're viewing our own profile or someone else's
        const userId = id || (currentUser ? currentUser.id : null);
        
        if (!userId) {
          toast.error('User not found');
          setLoading(false);
          return;
        }

        // Fetch user profile
        const userData = id 
          ? await userService.getUserById(userId)
          : await userService.getCurrentUserProfile();
        setUser(userData);

        // Fetch user skills
        const offeredSkillsData = await skillService.getOfferedSkillsByUser(userId);
        setOfferedSkills(offeredSkillsData);

        const wantedSkillsData = await skillService.getWantedSkillsByUser(userId);
        setWantedSkills(wantedSkillsData);

        // Fetch user feedback and rating
        const feedbackData = await feedbackService.getUserFeedback(userId);
        setFeedback(feedbackData);

        const ratingData = await feedbackService.getUserRating(userId);
        setRating(ratingData || 0);

      } catch (error) {
        console.error('Error fetching profile data:', error);
        toast.error('Failed to load profile data');
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [id, currentUser]);

  const renderStars = (rating) => {
    const stars = [];
    for (let i = 1; i <= 5; i++) {
      if (i <= rating) {
        stars.push(<FaStar key={i} className="text-yellow-500" />);
      } else {
        stars.push(<FaRegStar key={i} className="text-yellow-500" />);
      }
    }
    return stars;
  };

  if (loading) {
    return <div className="flex justify-center items-center h-screen">Loading...</div>;
  }

  if (!user) {
    return <div className="flex justify-center items-center h-screen">User not found</div>;
  }

  return (
    <div className="container mx-auto px-4 py-8">
      <div className="bg-white rounded-lg shadow-md overflow-hidden">
        {/* Profile Header */}
        <div className="bg-blue-600 text-white p-6">
          <div className="flex flex-col md:flex-row items-center">
            <div className="w-32 h-32 rounded-full overflow-hidden border-4 border-white mb-4 md:mb-0 md:mr-6">
              <img 
                src={user.profilePhoto || 'https://via.placeholder.com/150'} 
                alt={user.name} 
                className="w-full h-full object-cover"
              />
            </div>
            <div>
              <h1 className="text-3xl font-bold">{user.name}</h1>
              <p className="text-blue-200">@{user.username}</p>
              <div className="flex items-center mt-2">
                {renderStars(Math.round(rating))}
                <span className="ml-2">({rating.toFixed(1)})</span>
              </div>
              <p className="mt-2">{user.location}</p>
            </div>
          </div>
        </div>

        {/* Navigation Tabs */}
        <div className="border-b">
          <nav className="flex">
            <button 
              className={`px-4 py-3 font-medium ${activeTab === 'overview' ? 'text-blue-600 border-b-2 border-blue-600' : 'text-gray-500'}`}
              onClick={() => setActiveTab('overview')}
            >
              Overview
            </button>
            <button 
              className={`px-4 py-3 font-medium ${activeTab === 'skills' ? 'text-blue-600 border-b-2 border-blue-600' : 'text-gray-500'}`}
              onClick={() => setActiveTab('skills')}
            >
              Skills
            </button>
            <button 
              className={`px-4 py-3 font-medium ${activeTab === 'feedback' ? 'text-blue-600 border-b-2 border-blue-600' : 'text-gray-500'}`}
              onClick={() => setActiveTab('feedback')}
            >
              Feedback
            </button>
          </nav>
        </div>

        {/* Content */}
        <div className="p-6">
          {activeTab === 'overview' && (
            <div>
              <h2 className="text-xl font-semibold mb-4">About</h2>
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <h3 className="text-lg font-medium mb-2">Availability</h3>
                  <p>{user.availability}</p>
                </div>
                <div>
                  <h3 className="text-lg font-medium mb-2">Location</h3>
                  <p>{user.location || 'Not specified'}</p>
                </div>
              </div>
              
              <h2 className="text-xl font-semibold mt-6 mb-4">Skills Overview</h2>
              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div>
                  <h3 className="text-lg font-medium mb-2">Skills I Offer</h3>
                  {offeredSkills.length > 0 ? (
                    <ul className="list-disc pl-5">
                      {offeredSkills.slice(0, 5).map(skill => (
                        <li key={skill.id}>{skill.name}</li>
                      ))}
                    </ul>
                  ) : (
                    <p className="text-gray-500">No offered skills yet</p>
                  )}
                  {offeredSkills.length > 5 && (
                    <button 
                      className="text-blue-600 mt-2"
                      onClick={() => setActiveTab('skills')}
                    >
                      View all
                    </button>
                  )}
                </div>
                <div>
                  <h3 className="text-lg font-medium mb-2">Skills I Want</h3>
                  {wantedSkills.length > 0 ? (
                    <ul className="list-disc pl-5">
                      {wantedSkills.slice(0, 5).map(skill => (
                        <li key={skill.id}>{skill.name}</li>
                      ))}
                    </ul>
                  ) : (
                    <p className="text-gray-500">No wanted skills yet</p>
                  )}
                  {wantedSkills.length > 5 && (
                    <button 
                      className="text-blue-600 mt-2"
                      onClick={() => setActiveTab('skills')}
                    >
                      View all
                    </button>
                  )}
                </div>
              </div>
              
              <h2 className="text-xl font-semibold mt-6 mb-4">Recent Feedback</h2>
              {feedback.length > 0 ? (
                <div>
                  {feedback.slice(0, 3).map(item => (
                    <div key={item.id} className="border-b pb-4 mb-4">
                      <div className="flex items-center mb-2">
                        <span className="font-medium mr-2">From {item.reviewerUsername}</span>
                        <div className="flex">
                          {renderStars(item.rating)}
                        </div>
                      </div>
                      <p>{item.comment}</p>
                    </div>
                  ))}
                  {feedback.length > 3 && (
                    <button 
                      className="text-blue-600"
                      onClick={() => setActiveTab('feedback')}
                    >
                      View all feedback
                    </button>
                  )}
                </div>
              ) : (
                <p className="text-gray-500">No feedback yet</p>
              )}
            </div>
          )}

          {activeTab === 'skills' && (
            <div>
              <div className="mb-8">
                <h2 className="text-xl font-semibold mb-4">Skills I Offer</h2>
                {offeredSkills.length > 0 ? (
                  <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                    {offeredSkills.map(skill => (
                      <div key={skill.id} className="border rounded-lg p-4">
                        <h3 className="font-medium">{skill.name}</h3>
                        <p className="text-gray-600 text-sm mt-1">{skill.description}</p>
                      </div>
                    ))}
                  </div>
                ) : (
                  <p className="text-gray-500">No offered skills yet</p>
                )}
              </div>
              
              <div>
                <h2 className="text-xl font-semibold mb-4">Skills I Want</h2>
                {wantedSkills.length > 0 ? (
                  <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                    {wantedSkills.map(skill => (
                      <div key={skill.id} className="border rounded-lg p-4">
                        <h3 className="font-medium">{skill.name}</h3>
                        <p className="text-gray-600 text-sm mt-1">{skill.description}</p>
                      </div>
                    ))}
                  </div>
                ) : (
                  <p className="text-gray-500">No wanted skills yet</p>
                )}
              </div>
            </div>
          )}

          {activeTab === 'feedback' && (
            <div>
              <h2 className="text-xl font-semibold mb-4">All Feedback</h2>
              {feedback.length > 0 ? (
                <div>
                  {feedback.map(item => (
                    <div key={item.id} className="border-b pb-4 mb-4">
                      <div className="flex items-center mb-2">
                        <span className="font-medium mr-2">From {item.reviewerUsername}</span>
                        <div className="flex">
                          {renderStars(item.rating)}
                        </div>
                      </div>
                      <p>{item.comment}</p>
                      <p className="text-gray-500 text-sm mt-2">
                        {new Date(item.createdAt).toLocaleDateString()}
                      </p>
                    </div>
                  ))}
                </div>
              ) : (
                <p className="text-gray-500">No feedback yet</p>
              )}
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default Profile;