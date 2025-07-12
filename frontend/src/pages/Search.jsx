import React, { useState, useEffect } from 'react';
import { useLocation, Link } from 'react-router-dom';
import { FaSearch, FaUser, FaCheck, FaTimes } from 'react-icons/fa';
import skillService from '../services/skillService';

// Predefined skills list
const predefinedSkills = [
  'Java',
  'React',
  'HTML',
  'CSS',
  'JavaScript',
  'Python',
  'C',
  'C++',
  'C#',
  'PHP',
  'Ruby',
  'Swift',
  'Kotlin',
  'Go',
  'Rust',
  'TypeScript',
  'Angular',
  'Vue.js',
  'Node.js',
  'Express.js',
  'Django',
  'Flask',
  'Spring',
  'Hibernate',
  'SQL',
  'MongoDB',
  'PostgreSQL',
  'MySQL',
  'Firebase',
  'AWS',
  'Docker',
  'Kubernetes',
  'Git',
  'DevOps',
  'Machine Learning',
  'Artificial Intelligence',
  'Data Science',
  'Blockchain',
  'UI/UX Design',
  'Graphic Design',
  'Photography',
  'Video Editing',
  'Content Writing',
  'Digital Marketing',
  'SEO',
  'Social Media Marketing'
];

const Search = () => {
  const location = useLocation();
  const queryParams = new URLSearchParams(location.search);
  const initialSkill = queryParams.get('skill') || '';
  
  const [searchTerm, setSearchTerm] = useState(initialSkill);
  const [searchType, setSearchType] = useState('all'); // 'all', 'offered', 'wanted'
  const [results, setResults] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [selectedSkill, setSelectedSkill] = useState('');
  const [filteredSkills, setFilteredSkills] = useState([]);

  // Filter predefined skills based on search term
  useEffect(() => {
    if (searchTerm.trim() === '') {
      setFilteredSkills([]);
    } else {
      const filtered = predefinedSkills.filter(skill => 
        skill.toLowerCase().includes(searchTerm.toLowerCase())
      );
      setFilteredSkills(filtered.slice(0, 5)); // Limit to 5 suggestions
    }
  }, [searchTerm]);

  // Handle search submission
  const handleSearch = async (e) => {
    e?.preventDefault();
    
    if (!searchTerm.trim()) {
      setError('Please enter a skill to search for');
      return;
    }
    
    try {
      setLoading(true);
      setError(null);
      
      let searchResults;
      
      switch (searchType) {
        case 'offered':
          searchResults = await skillService.searchOfferedSkillsByName(searchTerm);
          break;
        case 'wanted':
          searchResults = await skillService.searchWantedSkillsByName(searchTerm);
          break;
        default:
          searchResults = await skillService.searchSkillsByName(searchTerm);
          break;
      }
      
      setResults(searchResults);
      
      // Update URL with search term
      const newUrl = `${window.location.pathname}?skill=${encodeURIComponent(searchTerm)}`;
      window.history.pushState({ path: newUrl }, '', newUrl);
      
    } catch (err) {
      console.error('Error searching skills:', err);
      setError('Failed to search skills. Please try again later.');
    } finally {
      setLoading(false);
    }
  };

  // Handle skill selection from predefined list
  const handleSkillSelect = (skill) => {
    setSearchTerm(skill);
    setSelectedSkill(skill);
    setFilteredSkills([]);
  };

  // Search on initial load if skill is provided in URL
  useEffect(() => {
    if (initialSkill) {
      handleSearch();
    }
  }, []);

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold text-gray-800 mb-8">Find Skills</h1>
      
      {/* Search Form */}
      <div className="bg-white rounded-lg shadow-md p-6 mb-8">
        <form onSubmit={handleSearch} className="space-y-4">
          <div className="relative">
            <label className="block text-gray-700 font-medium mb-2" htmlFor="searchTerm">
              Search for a skill
            </label>
            <div className="flex">
              <div className="relative flex-grow">
                <input
                  type="text"
                  id="searchTerm"
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  placeholder="Enter a skill name..."
                  className="w-full border border-gray-300 rounded-l px-3 py-2 focus:outline-none focus:ring-2 focus:ring-primary-500"
                />
                {filteredSkills.length > 0 && (
                  <div className="absolute z-10 w-full bg-white border border-gray-300 rounded-b shadow-lg mt-1">
                    {filteredSkills.map((skill, index) => (
                      <div
                        key={index}
                        className="px-4 py-2 hover:bg-gray-100 cursor-pointer"
                        onClick={() => handleSkillSelect(skill)}
                      >
                        {skill}
                      </div>
                    ))}
                  </div>
                )}
              </div>
              <button
                type="submit"
                className="bg-primary-600 text-white px-4 py-2 rounded-r hover:bg-primary-700 focus:outline-none focus:ring-2 focus:ring-primary-500"
                disabled={loading}
              >
                <FaSearch />
              </button>
            </div>
          </div>
          
          <div>
            <label className="block text-gray-700 font-medium mb-2">
              Search for
            </label>
            <div className="flex space-x-4">
              <label className="flex items-center">
                <input
                  type="radio"
                  name="searchType"
                  value="all"
                  checked={searchType === 'all'}
                  onChange={() => setSearchType('all')}
                  className="mr-2"
                />
                All Skills
              </label>
              <label className="flex items-center">
                <input
                  type="radio"
                  name="searchType"
                  value="offered"
                  checked={searchType === 'offered'}
                  onChange={() => setSearchType('offered')}
                  className="mr-2"
                />
                Offered Skills
              </label>
              <label className="flex items-center">
                <input
                  type="radio"
                  name="searchType"
                  value="wanted"
                  checked={searchType === 'wanted'}
                  onChange={() => setSearchType('wanted')}
                  className="mr-2"
                />
                Wanted Skills
              </label>
            </div>
          </div>
        </form>
      </div>
      
      {/* Predefined Skills */}
      <div className="bg-white rounded-lg shadow-md p-6 mb-8">
        <h2 className="text-xl font-semibold mb-4">Popular Skills</h2>
        <div className="flex flex-wrap gap-2">
          {predefinedSkills.slice(0, 15).map((skill, index) => (
            <button
              key={index}
              onClick={() => handleSkillSelect(skill)}
              className={`px-3 py-1 rounded-full text-sm ${
                selectedSkill === skill
                  ? 'bg-primary-600 text-white'
                  : 'bg-gray-100 text-gray-800 hover:bg-gray-200'
              }`}
            >
              {skill}
            </button>
          ))}
          <button
            onClick={() => setSelectedSkill('')}
            className="px-3 py-1 rounded-full text-sm bg-gray-100 text-gray-800 hover:bg-gray-200"
          >
            More...
          </button>
        </div>
      </div>
      
      {/* Error Message */}
      {error && (
        <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">
          {error}
        </div>
      )}
      
      {/* Loading Indicator */}
      {loading && (
        <div className="flex justify-center py-12">
          <div className="animate-spin rounded-full h-12 w-12 border-t-4 border-b-4 border-primary-600"></div>
        </div>
      )}
      
      {/* Search Results */}
      {!loading && results.length > 0 && (
        <div className="bg-white rounded-lg shadow-md p-6">
          <h2 className="text-xl font-semibold mb-4">Search Results</h2>
          <div className="divide-y">
            {results.map((skill) => (
              <div key={skill.id} className="py-4">
                <div className="flex items-start justify-between">
                  <div>
                    <div className="flex items-center">
                      <h3 className="font-medium text-lg">{skill.name}</h3>
                      <span className={`ml-2 px-2 py-0.5 text-xs rounded-full ${
                        skill.isOffered 
                          ? 'bg-green-100 text-green-800' 
                          : 'bg-blue-100 text-blue-800'
                      }`}>
                        {skill.isOffered ? (
                          <span className="flex items-center"><FaCheck className="mr-1" /> Offered</span>
                        ) : (
                          <span className="flex items-center"><FaTimes className="mr-1" /> Wanted</span>
                        )}
                      </span>
                    </div>
                    {skill.description && <p className="text-gray-600 mt-1">{skill.description}</p>}
                  </div>
                  <Link 
                    to={`/users/${skill.userId}`}
                    className="flex items-center text-primary-600 hover:text-primary-800"
                  >
                    <FaUser className="mr-1" /> {skill.username}
                  </Link>
                </div>
              </div>
            ))}
          </div>
        </div>
      )}
      
      {/* No Results */}
      {!loading && searchTerm && results.length === 0 && (
        <div className="bg-white rounded-lg shadow-md p-6 text-center">
          <p className="text-gray-600">No skills found matching "{searchTerm}".</p>
          <p className="mt-2">Try a different search term or browse the popular skills above.</p>
        </div>
      )}
    </div>
  );
};

export default Search;