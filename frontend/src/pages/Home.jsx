import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { FaSearch, FaExchangeAlt, FaUserFriends, FaStar } from 'react-icons/fa';
import { useAuth } from '../context/AuthContext.jsx';
import skillService from '../services/skillService';

const Home = () => {
  const { user } = useAuth();
  const [featuredSkills, setFeaturedSkills] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchFeaturedSkills = async () => {
      try {
        // Get some random skills to display
        const skills = await skillService.getAllSkillNames();
        setFeaturedSkills(skills.slice(0, 6));
      } catch (error) {
        console.error('Error fetching skills:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchFeaturedSkills();
  }, []);

  return (
    <div className="space-y-12">
      {/* Hero Section */}
      <section className="bg-gradient-to-r from-primary-700 to-primary-900 text-white py-20 px-4 rounded-xl shadow-2xl relative overflow-hidden">
        <div className="absolute inset-0 bg-black opacity-10"></div>
        <div className="absolute inset-0 bg-[url('https://images.unsplash.com/photo-1522202176988-66273c2fd55f?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1471&q=80')] bg-cover bg-center opacity-20 mix-blend-overlay"></div>
        <div className="max-w-4xl mx-auto text-center relative z-10">
          <div className="animate-fadeIn">
            <h1 className="text-4xl md:text-6xl font-bold mb-6 drop-shadow-md">
              Share Your Skills, Grow Together
            </h1>
            <p className="text-xl md:text-2xl mb-10 max-w-3xl mx-auto leading-relaxed">
              SkillSwap connects people who want to exchange skills and knowledge. 
              Teach what you know, learn what you don't.
            </p>
            <div className="flex flex-col sm:flex-row justify-center gap-6">
              {user ? (
                <>
                  <Link to="/search" className="btn bg-white text-primary-700 hover:bg-gray-100 transform hover:scale-105 transition-transform duration-300 shadow-lg text-lg py-3 px-6">
                    <FaSearch className="mr-2" /> Find Skills
                  </Link>
                  <Link to="/my-skills" className="btn bg-primary-600 text-white hover:bg-primary-700 border border-white transform hover:scale-105 transition-transform duration-300 shadow-lg text-lg py-3 px-6">
                    Manage My Skills
                  </Link>
                </>
              ) : (
                <>
                  <Link to="/register" className="btn bg-white text-primary-700 hover:bg-gray-100 transform hover:scale-105 transition-transform duration-300 shadow-lg text-lg py-3 px-6">
                    Join SkillSwap
                  </Link>
                  <Link to="/login" className="btn bg-primary-600 text-white hover:bg-primary-700 border border-white transform hover:scale-105 transition-transform duration-300 shadow-lg text-lg py-3 px-6">
                    Sign In
                  </Link>
                </>
              )}
            </div>
          </div>
        </div>
      </section>

      {/* How It Works */}
      <section className="py-16">
        <h2 className="text-3xl md:text-4xl font-bold text-center mb-12 relative">
          <span className="bg-clip-text text-transparent bg-gradient-to-r from-primary-600 to-secondary-600">How SkillSwap Works</span>
          <div className="absolute w-24 h-1 bg-gradient-to-r from-primary-500 to-secondary-500 bottom-0 left-1/2 transform -translate-x-1/2 mt-2 rounded-full"></div>
        </h2>
        <div className="grid md:grid-cols-3 gap-8 max-w-6xl mx-auto">
          <div className="card text-center hover:shadow-xl transition-shadow duration-300 border border-gray-100 rounded-xl overflow-hidden group">
            <div className="bg-gradient-to-br from-primary-100 to-primary-200 p-8">
              <div className="bg-white p-5 rounded-full w-20 h-20 flex items-center justify-center mx-auto mb-4 shadow-md group-hover:scale-110 transition-transform duration-300">
                <FaUserFriends className="text-primary-600 text-3xl" />
              </div>
            </div>
            <div className="p-6">
              <h3 className="text-xl font-semibold mb-3">Create Your Profile</h3>
              <p className="text-gray-600">
                Sign up and list the skills you can offer and the skills you want to learn.
              </p>
            </div>
            <div className="bg-gray-50 py-4 px-6">
              <span className="text-primary-600 font-medium">Step 1</span>
            </div>
          </div>
          <div className="card text-center hover:shadow-xl transition-shadow duration-300 border border-gray-100 rounded-xl overflow-hidden group">
            <div className="bg-gradient-to-br from-primary-100 to-primary-200 p-8">
              <div className="bg-white p-5 rounded-full w-20 h-20 flex items-center justify-center mx-auto mb-4 shadow-md group-hover:scale-110 transition-transform duration-300">
                <FaSearch className="text-primary-600 text-3xl" />
              </div>
            </div>
            <div className="p-6">
              <h3 className="text-xl font-semibold mb-3">Find Matches</h3>
              <p className="text-gray-600">
                Search for users who are offering the skills you want to learn.
              </p>
            </div>
            <div className="bg-gray-50 py-4 px-6">
              <span className="text-primary-600 font-medium">Step 2</span>
            </div>
          </div>
          <div className="card text-center hover:shadow-xl transition-shadow duration-300 border border-gray-100 rounded-xl overflow-hidden group">
            <div className="bg-gradient-to-br from-primary-100 to-primary-200 p-8">
              <div className="bg-white p-5 rounded-full w-20 h-20 flex items-center justify-center mx-auto mb-4 shadow-md group-hover:scale-110 transition-transform duration-300">
                <FaExchangeAlt className="text-primary-600 text-3xl" />
              </div>
            </div>
            <div className="p-6">
              <h3 className="text-xl font-semibold mb-3">Swap Skills</h3>
              <p className="text-gray-600">
                Send swap requests, arrange sessions, and exchange knowledge.
              </p>
            </div>
            <div className="bg-gray-50 py-4 px-6">
              <span className="text-primary-600 font-medium">Step 3</span>
            </div>
          </div>
        </div>
      </section>

      {/* Featured Skills */}
      <section className="py-16 bg-gradient-to-b from-white to-gray-50 rounded-xl p-8 shadow-lg">
        <h2 className="text-3xl md:text-4xl font-bold text-center mb-10 relative">
          <span className="bg-clip-text text-transparent bg-gradient-to-r from-primary-600 to-secondary-600">Popular Skills on SkillSwap</span>
          <div className="absolute w-24 h-1 bg-gradient-to-r from-primary-500 to-secondary-500 bottom-0 left-1/2 transform -translate-x-1/2 mt-2 rounded-full"></div>
        </h2>
        {loading ? (
          <div className="flex justify-center py-12">
            <div className="animate-spin rounded-full h-16 w-16 border-t-4 border-b-4 border-primary-600"></div>
          </div>
        ) : (
          <div className="grid grid-cols-2 md:grid-cols-3 gap-6 max-w-5xl mx-auto">
            {featuredSkills.map((skill, index) => (
              <div key={index} className="bg-white p-6 rounded-xl shadow-md hover:shadow-xl transition-all duration-300 transform hover:-translate-y-1 border border-gray-100">
                <Link 
                  to={`/search?skill=${encodeURIComponent(skill)}`} 
                  className="text-primary-600 hover:text-primary-800 font-medium text-lg flex items-center justify-between"
                >
                  <span>{skill}</span>
                  <span className="text-gray-400 hover:text-primary-600 transition-colors">
                    <FaSearch className="ml-2" />
                  </span>
                </Link>
              </div>
            ))}
          </div>
        )}
        <div className="text-center mt-12">
          <Link to="/search" className="btn btn-primary text-lg py-3 px-8 rounded-full shadow-lg transform hover:scale-105 transition-transform duration-300">
            Explore All Skills
          </Link>
        </div>
      </section>

      {/* Testimonials */}
      <section className="py-16 bg-gray-50 rounded-2xl my-8">
        <h2 className="text-3xl md:text-4xl font-bold text-center mb-12 relative">
          <span className="bg-clip-text text-transparent bg-gradient-to-r from-primary-600 to-secondary-600">What Our Users Say</span>
          <div className="absolute w-24 h-1 bg-gradient-to-r from-primary-500 to-secondary-500 bottom-0 left-1/2 transform -translate-x-1/2 mt-2 rounded-full"></div>
        </h2>
        <div className="grid md:grid-cols-2 gap-8 max-w-5xl mx-auto px-4">
          <div className="bg-white rounded-xl shadow-lg p-8 relative hover:shadow-xl transition-shadow duration-300">
            <div className="absolute -top-5 -left-5 w-10 h-10 bg-primary-500 rounded-full flex items-center justify-center text-white text-2xl">
              "
            </div>
            <div className="flex items-center mb-6">
              <img 
                src="https://randomuser.me/api/portraits/women/44.jpg" 
                alt="Sarah Johnson" 
                className="w-16 h-16 rounded-full object-cover border-4 border-primary-100 mr-4"
              />
              <div>
                <h4 className="font-semibold text-lg">Sarah Johnson</h4>
                <p className="text-gray-500 text-sm">Spanish Teacher</p>
                <div className="flex text-yellow-400 mt-1">
                  <FaStar />
                  <FaStar />
                  <FaStar />
                  <FaStar />
                  <FaStar />
                </div>
              </div>
            </div>
            <p className="text-gray-600 italic">
              "I've been teaching Spanish and learning web development. SkillSwap made it easy to find the perfect exchange partner. The platform is intuitive and the community is supportive. Highly recommended!"
            </p>
          </div>
          <div className="bg-white rounded-xl shadow-lg p-8 relative hover:shadow-xl transition-shadow duration-300">
            <div className="absolute -top-5 -left-5 w-10 h-10 bg-primary-500 rounded-full flex items-center justify-center text-white text-2xl">
              "
            </div>
            <div className="flex items-center mb-6">
              <img 
                src="https://randomuser.me/api/portraits/men/32.jpg" 
                alt="Michael Chen" 
                className="w-16 h-16 rounded-full object-cover border-4 border-primary-100 mr-4"
              />
              <div>
                <h4 className="font-semibold text-lg">Michael Chen</h4>
                <p className="text-gray-500 text-sm">Photographer</p>
                <div className="flex text-yellow-400 mt-1">
                  <FaStar />
                  <FaStar />
                  <FaStar />
                  <FaStar />
                  <FaStar />
                </div>
              </div>
            </div>
            <p className="text-gray-600 italic">
              "As a photographer, I wanted to learn digital marketing. Through SkillSwap, I found someone who needed photography lessons and could teach me marketing. The skill exchange was seamless and beneficial for both of us. Win-win!"
            </p>
          </div>
        </div>
      </section>

      {/* CTA */}
      <section className="bg-gradient-to-r from-secondary-600 to-secondary-800 text-white py-16 px-4 rounded-xl text-center shadow-2xl relative overflow-hidden">
        <div className="absolute inset-0 bg-black opacity-10"></div>
        <div className="absolute inset-0">
          <div className="absolute top-0 left-0 w-full h-20 bg-white opacity-5 transform -skew-y-6"></div>
          <div className="absolute bottom-0 right-0 w-full h-20 bg-white opacity-5 transform skew-y-6"></div>
        </div>
        <div className="relative z-10">
          <h2 className="text-3xl md:text-4xl font-bold mb-6 drop-shadow-md">Ready to Start Swapping Skills?</h2>
          <p className="text-xl md:text-2xl mb-10 max-w-3xl mx-auto leading-relaxed">
            Join our community of skill-sharers today and start your learning journey.
          </p>
          {user ? (
            <Link to="/search" className="btn bg-white text-secondary-600 hover:bg-gray-100 transform hover:scale-105 transition-transform duration-300 shadow-lg text-lg py-3 px-8 rounded-full">
              Find Skills Now
            </Link>
          ) : (
            <Link to="/register" className="btn bg-white text-secondary-600 hover:bg-gray-100 transform hover:scale-105 transition-transform duration-300 shadow-lg text-lg py-3 px-8 rounded-full">
              Sign Up for Free
            </Link>
          )}
        </div>
      </section>
    </div>
  );
};

export default Home;
