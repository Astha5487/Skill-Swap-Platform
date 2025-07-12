                                         import React from 'react';
                                         import { Link } from 'react-router-dom';
                                         import { FaExchangeAlt, FaGithub, FaTwitter, FaLinkedin } from 'react-icons/fa';

                                         const Footer = () => {
                                           const currentYear = new Date().getFullYear();

                                           return (
                                             <footer className="bg-gray-800 text-white py-8">
                                               <div className="container mx-auto px-4">
                                                 <div className="flex flex-col md:flex-row justify-between items-center">
                                                   {/* Logo and copyright */}
                                                   <div className="mb-6 md:mb-0">
                                                     <Link to="/" className="flex items-center text-xl font-bold mb-2">
                                                       <FaExchangeAlt className="mr-2" />
                                                       SkillSwap
                                                     </Link>
                                                     <p className="text-gray-400 text-sm">
                                                       &copy; {currentYear} SkillSwap. All rights reserved.
                                                     </p>
                                                   </div>

                                                   {/* Links */}
                                                   <div className="grid grid-cols-2 gap-8 sm:grid-cols-3 mb-6 md:mb-0">
                                                     <div>
                                                       <h3 className="text-lg font-semibold mb-2">Platform</h3>
                                                       <ul className="space-y-2">
                                                         <li>
                                                           <Link to="/" className="text-gray-400 hover:text-white transition-colors">
                                                             Home
                                                           </Link>
                                                         </li>
                                                         <li>
                                                           <Link to="/search" className="text-gray-400 hover:text-white transition-colors">
                                                             Search Skills
                                                           </Link>
                                                         </li>
                                                         <li>
                                                           <Link to="/swap-requests" className="text-gray-400 hover:text-white transition-colors">
                                                             Swap Requests
                                                           </Link>
                                                         </li>
                                                       </ul>
                                                     </div>

                                                     <div>
                                                       <h3 className="text-lg font-semibold mb-2">Account</h3>
                                                       <ul className="space-y-2">
                                                         <li>
                                                           <Link to="/login" className="text-gray-400 hover:text-white transition-colors">
                                                             Login
                                                           </Link>
                                                         </li>
                                                         <li>
                                                           <Link to="/register" className="text-gray-400 hover:text-white transition-colors">
                                                             Register
                                                           </Link>
                                                         </li>
                                                         <li>
                                                           <Link to="/profile" className="text-gray-400 hover:text-white transition-colors">
                                                             My Profile
                                                           </Link>
                                                         </li>
                                                       </ul>
                                                     </div>

                                                     <div>
                                                       <h3 className="text-lg font-semibold mb-2">Legal</h3>
                                                       <ul className="space-y-2">
                                                         <li>
                                                           <Link to="/privacy" className="text-gray-400 hover:text-white transition-colors">
                                                             Privacy Policy
                                                           </Link>
                                                         </li>
                                                         <li>
                                                           <Link to="/terms" className="text-gray-400 hover:text-white transition-colors">
                                                             Terms of Service
                                                           </Link>
                                                         </li>
                                                         <li>
                                                           <Link to="/contact" className="text-gray-400 hover:text-white transition-colors">
                                                             Contact Us
                                                           </Link>
                                                         </li>
                                                       </ul>
                                                     </div>
                                                   </div>

                                                   {/* Social links */}
                                                   <div className="flex space-x-4">
                                                     <a
                                                       href="https://github.com"
                                                       target="_blank"
                                                       rel="noopener noreferrer"
                                                       className="text-gray-400 hover:text-white transition-colors"
                                                     >
                                                       <FaGithub size={24} />
                                                     </a>
                                                     <a
                                                       href="https://twitter.com"
                                                       target="_blank"
                                                       rel="noopener noreferrer"
                                                       className="text-gray-400 hover:text-white transition-colors"
                                                     >
                                                       <FaTwitter size={24} />
                                                     </a>
                                                     <a
                                                       href="https://linkedin.com"
                                                       target="_blank"
                                                       rel="noopener noreferrer"
                                                       className="text-gray-400 hover:text-white transition-colors"
                                                     >
                                                       <FaLinkedin size={24} />
                                                     </a>
                                                   </div>
                                                 </div>
                                               </div>
                                             </footer>
                                           );
                                         };

                                         export default Footer;