import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { FaSearch, FaExchangeAlt, FaUserFriends, FaStar } from 'react-icons/fa';
import { useAuth } from '../context/AuthContext';
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
      <section className="bg-gradient-to-r from-primary-700 to-primary-900 text-white py-16 px-4 rounded-lg">
        <div className="max-w-4xl mx-auto text-center">
          <h1 className="text-4xl md:text-5xl font-bold mb-6">
            Share Your Skills, Grow Together
          </h1>
          <p className="text-xl mb-8">
            SkillSwap connects people who want to exchange skills and knowledge. 
            Teach what you know, learn what you don't.
          </p>
          <div className="flex flex-col sm:flex-row justify-center gap-4">
            {user ? (
              <>
                <Link to="/search" className="btn bg-white text-primary-700 hover:bg-gray-100">
                  <FaSearch className="mr-2" /> Find Skills
                </Link>
                <Link to="/my-skills" className="btn bg-primary-600 text-white hover:bg-primary-700 border border-white">
                  Manage My Skills
                </Link>
              </>
            ) : (
              <>
                <Link to="/register" className="btn bg-white text-primary-700 hover:bg-gray-100">
                  Join SkillSwap
                </Link>
                <Link to="/login" className="btn bg-primary-600 text-white hover:bg-primary-700 border border-white">
                  Sign In
                </Link>
              </>
            )}
          </div>
        </div>
      </section>

      {/* How It Works */}
      <section className="py-12">
        <h2 className="text-3xl font-bold text-center mb-12">How SkillSwap Works</h2>
        <div className="grid md:grid-cols-3 gap-8">
          <div className="card text-center">
            <div className="bg-primary-100 p-4 rounded-full w-16 h-16 flex items-center justify-center mx-auto mb-4">
              <FaUserFriends className="text-primary-600 text-2xl" />
            </div>
            <h3 className="text-xl font-semibold mb-2">Create Your Profile</h3>
            <p className="text-gray-600">
              Sign up and list the skills you can offer and the skills you want to learn.
            </p>
          </div>
          <div className="card text-center">
            <div className="bg-primary-100 p-4 rounded-full w-16 h-16 flex items-center justify-center mx-auto mb-4">
              <FaSearch className="text-primary-600 text-2xl" />
            </div>
            <h3 className="text-xl font-semibold mb-2">Find Matches</h3>
            <p className="text-gray-600">
              Search for users who are offering the skills you want to learn.
            </p>
          </div>
          <div className="card text-center">
            <div className="bg-primary-100 p-4 rounded-full w-16 h-16 flex items-center justify-center mx-auto mb-4">
              <FaExchangeAlt className="text-primary-600 text-2xl" />
            </div>
            <h3 className="text-xl font-semibold mb-2">Swap Skills</h3>
            <p className="text-gray-600">
              Send swap requests, arrange sessions, and exchange knowledge.
            </p>
          </div>
        </div>
      </section>

      {/* Featured Skills */}
      <section className="py-12 bg-gray-50 rounded-lg p-8">
        <h2 className="text-3xl font-bold text-center mb-8">Popular Skills on SkillSwap</h2>
        {loading ? (
          <div className="flex justify-center">
            <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-primary-600"></div>
          </div>
        ) : (
          <div className="grid grid-cols-2 md:grid-cols-3 gap-4">
            {featuredSkills.map((skill, index) => (
              <div key={index} className="bg-white p-4 rounded-lg shadow-sm hover:shadow-md transition-shadow">
                <Link to={`/search?skill=${encodeURIComponent(skill)}`} className="text-primary-600 hover:text-primary-800 font-medium">
                  {skill}
                </Link>
              </div>
            ))}
          </div>
        )}
        <div className="text-center mt-8">
          <Link to="/search" className="btn btn-primary">
            Explore All Skills
          </Link>
        </div>
      </section>

      {/* Testimonials */}
      <section className="py-12">
        <h2 className="text-3xl font-bold text-center mb-12">What Our Users Say</h2>
        <div className="grid md:grid-cols-2 gap-8">
          <div className="card">
            <div className="flex items-center mb-4">
              <div className="bg-gray-200 rounded-full w-12 h-12 mr-4"></div>
              <div>
                <h4 className="font-semibold">Sarah Johnson</h4>
                <div className="flex text-yellow-400">
                  <FaStar />
                  <FaStar />
                  <FaStar />
                  <FaStar />
                  <FaStar />
                </div>
              </div>
            </div>
            <p className="text-gray-600">
              "I've been teaching Spanish and learning web development. SkillSwap made it easy to find the perfect exchange partner. Highly recommended!"
            </p>
          </div>
          <div className="card">
            <div className="flex items-center mb-4">
              <div className="bg-gray-200 rounded-full w-12 h-12 mr-4"></div>
              <div>
                <h4 className="font-semibold">Michael Chen</h4>
                <div className="flex text-yellow-400">
                  <FaStar />
                  <FaStar />
                  <FaStar />
                  <FaStar />
                  <FaStar />
                </div>
              </div>
            </div>
            <p className="text-gray-600">
              "As a photographer, I wanted to learn digital marketing. Through SkillSwap, I found someone who needed photography lessons and could teach me marketing. Win-win!"
            </p>
          </div>
        </div>
      </section>

      {/* CTA */}
      <section className="bg-secondary-600 text-white py-12 px-4 rounded-lg text-center">
        <h2 className="text-3xl font-bold mb-4">Ready to Start Swapping Skills?</h2>
        <p className="text-xl mb-8 max-w-2xl mx-auto">
          Join our community of skill-sharers today and start your learning journey.
        </p>
        {user ? (
          <Link to="/search" className="btn bg-white text-secondary-600 hover:bg-gray-100">
            Find Skills Now
          </Link>
        ) : (
          <Link to="/register" className="btn bg-white text-secondary-600 hover:bg-gray-100">
            Sign Up for Free
          </Link>
        )}
      </section>
    </div>
  );
};

export default Home;