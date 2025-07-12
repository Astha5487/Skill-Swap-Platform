import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { FaUser, FaSignOutAlt, FaSignInAlt, FaUserPlus, FaExchangeAlt, FaSearch, FaBars, FaTimes } from 'react-icons/fa';

const Navbar = () => {
  const { user, logout, isAdmin } = useAuth();
  const navigate = useNavigate();
  const [isMenuOpen, setIsMenuOpen] = useState(false);

  const toggleMenu = () => {
    setIsMenuOpen(!isMenuOpen);
  };

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <nav className="bg-primary-700 text-white shadow-md">
      <div className="container mx-auto px-4">
        <div className="flex justify-between items-center py-4">
          {/* Logo */}
          <Link to="/" className="text-2xl font-bold flex items-center">
            <FaExchangeAlt className="mr-2" />
            SkillSwap
          </Link>

          {/* Mobile menu button */}
          <button
            className="md:hidden text-white focus:outline-none"
            onClick={toggleMenu}
          >
            {isMenuOpen ? <FaTimes size={24} /> : <FaBars size={24} />}
          </button>

          {/* Desktop Navigation */}
          <div className="hidden md:flex items-center space-x-4">
            <Link to="/" className="hover:text-primary-200 transition-colors">
              Home
            </Link>
            <Link to="/search" className="hover:text-primary-200 transition-colors flex items-center">
              <FaSearch className="mr-1" /> Search Skills
            </Link>

            {user ? (
              <>
                <Link to="/swap-requests" className="hover:text-primary-200 transition-colors">
                  Swap Requests
                </Link>
                <div className="relative group">
                  <button className="flex items-center hover:text-primary-200 transition-colors">
                    <FaUser className="mr-1" /> {user.username}
                  </button>
                  <div className="absolute right-0 mt-2 w-48 bg-white rounded-md shadow-lg py-1 z-10 hidden group-hover:block">
                    <Link to="/profile" className="block px-4 py-2 text-gray-800 hover:bg-primary-50">
                      My Profile
                    </Link>
                    <Link to="/my-skills" className="block px-4 py-2 text-gray-800 hover:bg-primary-50">
                      My Skills
                    </Link>
                    {isAdmin() && (
                      <Link to="/admin" className="block px-4 py-2 text-gray-800 hover:bg-primary-50">
                        Admin Dashboard
                      </Link>
                    )}
                    <button
                      onClick={handleLogout}
                      className="w-full text-left px-4 py-2 text-gray-800 hover:bg-primary-50 flex items-center"
                    >
                      <FaSignOutAlt className="mr-2" /> Logout
                    </button>
                  </div>
                </div>
              </>
            ) : (
              <>
                <Link to="/login" className="hover:text-primary-200 transition-colors flex items-center">
                  <FaSignInAlt className="mr-1" /> Login
                </Link>
                <Link to="/register" className="bg-white text-primary-700 px-4 py-2 rounded-md hover:bg-primary-50 transition-colors flex items-center">
                  <FaUserPlus className="mr-1" /> Register
                </Link>
              </>
            )}
          </div>
        </div>

        {/* Mobile Navigation */}
        {isMenuOpen && (
          <div className="md:hidden pb-4">
            <Link to="/" className="block py-2 hover:text-primary-200 transition-colors" onClick={toggleMenu}>
              Home
            </Link>
            <Link to="/search" className="block py-2 hover:text-primary-200 transition-colors" onClick={toggleMenu}>
              Search Skills
            </Link>

            {user ? (
              <>
                <Link to="/swap-requests" className="block py-2 hover:text-primary-200 transition-colors" onClick={toggleMenu}>
                  Swap Requests
                </Link>
                <Link to="/profile" className="block py-2 hover:text-primary-200 transition-colors" onClick={toggleMenu}>
                  My Profile
                </Link>
                <Link to="/my-skills" className="block py-2 hover:text-primary-200 transition-colors" onClick={toggleMenu}>
                  My Skills
                </Link>
                {isAdmin() && (
                  <Link to="/admin" className="block py-2 hover:text-primary-200 transition-colors" onClick={toggleMenu}>
                    Admin Dashboard
                  </Link>
                )}
                <button
                  onClick={() => {
                    handleLogout();
                    toggleMenu();
                  }}
                  className="w-full text-left py-2 hover:text-primary-200 transition-colors flex items-center"
                >
                  <FaSignOutAlt className="mr-2" /> Logout
                </button>
              </>
            ) : (
              <>
                <Link to="/login" className="block py-2 hover:text-primary-200 transition-colors" onClick={toggleMenu}>
                  Login
                </Link>
                <Link to="/register" className="block py-2 hover:text-primary-200 transition-colors" onClick={toggleMenu}>
                  Register
                </Link>
              </>
            )}
          </div>
        )}
      </div>
    </nav>
  );
};

export default Navbar;