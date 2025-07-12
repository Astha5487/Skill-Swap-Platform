import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { FaUserPlus, FaExchangeAlt } from 'react-icons/fa';
import { useAuth } from '../context/AuthContext';

const Register = () => {
  const { register: registerUser } = useAuth();
  const navigate = useNavigate();
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState('');
  
  const { register, handleSubmit, formState: { errors }, watch } = useForm();
  const password = watch('password', '');

  const onSubmit = async (data) => {
    setIsLoading(true);
    setError('');
    
    try {
      // Create user data object
      const userData = {
        username: data.username,
        password: data.password,
        name: data.name,
        location: data.location || '',
        profilePhoto: '',
        availability: data.availability,
        isPublic: true
      };
      
      const success = await registerUser(userData);
      if (success) {
        navigate('/');
      }
    } catch (err) {
      setError(err.response?.data || 'Failed to register. Please try again.');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="flex flex-col items-center justify-center px-6 py-8 mx-auto">
      <div className="w-full bg-white rounded-lg shadow-md md:mt-0 sm:max-w-md xl:p-0">
        <div className="p-6 space-y-4 md:space-y-6 sm:p-8">
          <div className="flex justify-center">
            <FaExchangeAlt className="text-primary-600 text-4xl" />
          </div>
          <h1 className="text-xl font-bold leading-tight tracking-tight text-gray-900 md:text-2xl text-center">
            Create an account
          </h1>
          
          {error && (
            <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative" role="alert">
              <span className="block sm:inline">{error}</span>
            </div>
          )}
          
          <form className="space-y-4 md:space-y-6" onSubmit={handleSubmit(onSubmit)}>
            <div>
              <label htmlFor="username" className="block mb-2 text-sm font-medium text-gray-900">
                Username
              </label>
              <input
                type="text"
                id="username"
                className="input"
                placeholder="Your username"
                {...register('username', { 
                  required: 'Username is required',
                  minLength: {
                    value: 3,
                    message: 'Username must be at least 3 characters'
                  }
                })}
              />
              {errors.username && (
                <p className="text-red-500 text-xs mt-1">{errors.username.message}</p>
              )}
            </div>
            
            <div>
              <label htmlFor="name" className="block mb-2 text-sm font-medium text-gray-900">
                Full Name
              </label>
              <input
                type="text"
                id="name"
                className="input"
                placeholder="Your full name"
                {...register('name', { 
                  required: 'Full name is required'
                })}
              />
              {errors.name && (
                <p className="text-red-500 text-xs mt-1">{errors.name.message}</p>
              )}
            </div>
            
            <div>
              <label htmlFor="location" className="block mb-2 text-sm font-medium text-gray-900">
                Location (Optional)
              </label>
              <input
                type="text"
                id="location"
                className="input"
                placeholder="Your location"
                {...register('location')}
              />
            </div>
            
            <div>
              <label htmlFor="availability" className="block mb-2 text-sm font-medium text-gray-900">
                Availability
              </label>
              <select
                id="availability"
                className="input"
                {...register('availability', { 
                  required: 'Availability is required'
                })}
              >
                <option value="">Select your availability</option>
                <option value="Weekdays">Weekdays</option>
                <option value="Weekends">Weekends</option>
                <option value="Evenings">Evenings</option>
                <option value="Flexible">Flexible</option>
              </select>
              {errors.availability && (
                <p className="text-red-500 text-xs mt-1">{errors.availability.message}</p>
              )}
            </div>
            
            <div>
              <label htmlFor="password" className="block mb-2 text-sm font-medium text-gray-900">
                Password
              </label>
              <input
                type="password"
                id="password"
                className="input"
                placeholder="••••••••"
                {...register('password', { 
                  required: 'Password is required',
                  minLength: {
                    value: 6,
                    message: 'Password must be at least 6 characters'
                  }
                })}
              />
              {errors.password && (
                <p className="text-red-500 text-xs mt-1">{errors.password.message}</p>
              )}
            </div>
            
            <div>
              <label htmlFor="confirmPassword" className="block mb-2 text-sm font-medium text-gray-900">
                Confirm Password
              </label>
              <input
                type="password"
                id="confirmPassword"
                className="input"
                placeholder="••••••••"
                {...register('confirmPassword', { 
                  required: 'Please confirm your password',
                  validate: value => value === password || 'Passwords do not match'
                })}
              />
              {errors.confirmPassword && (
                <p className="text-red-500 text-xs mt-1">{errors.confirmPassword.message}</p>
              )}
            </div>
            
            <button
              type="submit"
              className="btn btn-primary w-full flex justify-center items-center"
              disabled={isLoading}
            >
              {isLoading ? (
                <div className="animate-spin rounded-full h-5 w-5 border-t-2 border-b-2 border-white"></div>
              ) : (
                <>
                  <FaUserPlus className="mr-2" /> Create Account
                </>
              )}
            </button>
            
            <p className="text-sm font-light text-gray-500">
              Already have an account?{' '}
              <Link to="/login" className="font-medium text-primary-600 hover:underline">
                Sign in
              </Link>
            </p>
          </form>
        </div>
      </div>
    </div>
  );
};

export default Register;