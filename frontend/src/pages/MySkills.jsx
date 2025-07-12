import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext.jsx';
import skillService from '../services/skillService';
import { FaPlus, FaEdit, FaTrash, FaCheck, FaTimes } from 'react-icons/fa';

const MySkills = () => {
  const { user } = useAuth();
  const [skills, setSkills] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  
  // Form state
  const [showForm, setShowForm] = useState(false);
  const [formMode, setFormMode] = useState('add'); // 'add' or 'edit'
  const [currentSkill, setCurrentSkill] = useState({
    id: null,
    name: '',
    description: '',
    isOffered: true
  });

  // Fetch user's skills
  useEffect(() => {
    const fetchSkills = async () => {
      try {
        setLoading(true);
        const data = await skillService.getSkillsByUser(user.id);
        setSkills(data);
        setError(null);
      } catch (err) {
        console.error('Error fetching skills:', err);
        setError('Failed to load skills. Please try again later.');
      } finally {
        setLoading(false);
      }
    };

    if (user?.id) {
      fetchSkills();
    }
  }, [user]);

  // Handle form input changes
  const handleInputChange = (e) => {
    const { name, value, type, checked } = e.target;
    setCurrentSkill({
      ...currentSkill,
      [name]: type === 'checkbox' ? checked : value
    });
  };

  // Open form for adding a new skill
  const handleAddSkill = () => {
    setCurrentSkill({
      id: null,
      name: '',
      description: '',
      isOffered: true
    });
    setFormMode('add');
    setShowForm(true);
  };

  // Open form for editing an existing skill
  const handleEditSkill = (skill) => {
    setCurrentSkill({
      id: skill.id,
      name: skill.name,
      description: skill.description || '',
      isOffered: skill.isOffered
    });
    setFormMode('edit');
    setShowForm(true);
  };

  // Handle form submission
  const handleSubmit = async (e) => {
    e.preventDefault();
    
    try {
      setLoading(true);
      
      if (formMode === 'add') {
        // Add new skill
        const newSkill = await skillService.addSkill({
          name: currentSkill.name,
          description: currentSkill.description,
          isOffered: currentSkill.isOffered,
          userId: user.id
        });
        
        setSkills([...skills, newSkill]);
      } else {
        // Update existing skill
        const updatedSkill = await skillService.updateSkill(currentSkill.id, {
          name: currentSkill.name,
          description: currentSkill.description,
          isOffered: currentSkill.isOffered,
          userId: user.id
        });
        
        setSkills(skills.map(skill => 
          skill.id === updatedSkill.id ? updatedSkill : skill
        ));
      }
      
      setShowForm(false);
      setError(null);
    } catch (err) {
      console.error('Error saving skill:', err);
      setError('Failed to save skill. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  // Handle skill deletion
  const handleDeleteSkill = async (skillId) => {
    if (!window.confirm('Are you sure you want to delete this skill?')) {
      return;
    }
    
    try {
      setLoading(true);
      await skillService.deleteSkill(skillId);
      setSkills(skills.filter(skill => skill.id !== skillId));
      setError(null);
    } catch (err) {
      console.error('Error deleting skill:', err);
      setError('Failed to delete skill. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  // Group skills by type (offered/wanted)
  const offeredSkills = skills.filter(skill => skill.isOffered);
  const wantedSkills = skills.filter(skill => !skill.isOffered);

  return (
    <div className="container mx-auto px-4 py-8">
      <div className="flex justify-between items-center mb-8">
        <h1 className="text-3xl font-bold text-gray-800">My Skills</h1>
        <button 
          onClick={handleAddSkill}
          className="btn btn-primary flex items-center"
          disabled={loading}
        >
          <FaPlus className="mr-2" /> Add Skill
        </button>
      </div>

      {error && (
        <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">
          {error}
        </div>
      )}

      {loading && !showForm ? (
        <div className="flex justify-center py-12">
          <div className="animate-spin rounded-full h-12 w-12 border-t-4 border-b-4 border-primary-600"></div>
        </div>
      ) : (
        <div className="grid md:grid-cols-2 gap-8">
          {/* Offered Skills */}
          <div className="bg-white rounded-lg shadow-md p-6">
            <h2 className="text-xl font-semibold mb-4 text-primary-600 flex items-center">
              <FaCheck className="mr-2" /> Skills I Offer
            </h2>
            {offeredSkills.length === 0 ? (
              <p className="text-gray-500 italic">You haven't added any skills you offer yet.</p>
            ) : (
              <ul className="space-y-4">
                {offeredSkills.map(skill => (
                  <li key={skill.id} className="border-b pb-3">
                    <div className="flex justify-between items-start">
                      <div>
                        <h3 className="font-medium text-lg">{skill.name}</h3>
                        {skill.description && <p className="text-gray-600 mt-1">{skill.description}</p>}
                        {!skill.isApproved && (
                          <span className="text-xs bg-yellow-100 text-yellow-800 px-2 py-1 rounded mt-2 inline-block">
                            Pending Approval
                          </span>
                        )}
                      </div>
                      <div className="flex space-x-2">
                        <button 
                          onClick={() => handleEditSkill(skill)}
                          className="text-blue-500 hover:text-blue-700"
                        >
                          <FaEdit />
                        </button>
                        <button 
                          onClick={() => handleDeleteSkill(skill.id)}
                          className="text-red-500 hover:text-red-700"
                        >
                          <FaTrash />
                        </button>
                      </div>
                    </div>
                  </li>
                ))}
              </ul>
            )}
          </div>

          {/* Wanted Skills */}
          <div className="bg-white rounded-lg shadow-md p-6">
            <h2 className="text-xl font-semibold mb-4 text-secondary-600 flex items-center">
              <FaTimes className="mr-2" /> Skills I Want to Learn
            </h2>
            {wantedSkills.length === 0 ? (
              <p className="text-gray-500 italic">You haven't added any skills you want to learn yet.</p>
            ) : (
              <ul className="space-y-4">
                {wantedSkills.map(skill => (
                  <li key={skill.id} className="border-b pb-3">
                    <div className="flex justify-between items-start">
                      <div>
                        <h3 className="font-medium text-lg">{skill.name}</h3>
                        {skill.description && <p className="text-gray-600 mt-1">{skill.description}</p>}
                      </div>
                      <div className="flex space-x-2">
                        <button 
                          onClick={() => handleEditSkill(skill)}
                          className="text-blue-500 hover:text-blue-700"
                        >
                          <FaEdit />
                        </button>
                        <button 
                          onClick={() => handleDeleteSkill(skill.id)}
                          className="text-red-500 hover:text-red-700"
                        >
                          <FaTrash />
                        </button>
                      </div>
                    </div>
                  </li>
                ))}
              </ul>
            )}
          </div>
        </div>
      )}

      {/* Add/Edit Skill Form */}
      {showForm && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
          <div className="bg-white rounded-lg shadow-lg p-6 w-full max-w-md">
            <h2 className="text-2xl font-bold mb-4">
              {formMode === 'add' ? 'Add New Skill' : 'Edit Skill'}
            </h2>
            
            <form onSubmit={handleSubmit}>
              <div className="mb-4">
                <label className="block text-gray-700 font-medium mb-2" htmlFor="name">
                  Skill Name*
                </label>
                <input
                  type="text"
                  id="name"
                  name="name"
                  value={currentSkill.name}
                  onChange={handleInputChange}
                  className="w-full border border-gray-300 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-primary-500"
                  required
                  maxLength={30}
                />
              </div>
              
              <div className="mb-4">
                <label className="block text-gray-700 font-medium mb-2" htmlFor="description">
                  Description (Optional)
                </label>
                <textarea
                  id="description"
                  name="description"
                  value={currentSkill.description}
                  onChange={handleInputChange}
                  className="w-full border border-gray-300 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-primary-500"
                  rows="3"
                  maxLength={200}
                ></textarea>
              </div>
              
              <div className="mb-6">
                <label className="block text-gray-700 font-medium mb-2">
                  Skill Type*
                </label>
                <div className="flex space-x-4">
                  <label className="flex items-center">
                    <input
                      type="radio"
                      name="isOffered"
                      checked={currentSkill.isOffered}
                      onChange={() => setCurrentSkill({...currentSkill, isOffered: true})}
                      className="mr-2"
                    />
                    I offer this skill
                  </label>
                  <label className="flex items-center">
                    <input
                      type="radio"
                      name="isOffered"
                      checked={!currentSkill.isOffered}
                      onChange={() => setCurrentSkill({...currentSkill, isOffered: false})}
                      className="mr-2"
                    />
                    I want to learn this skill
                  </label>
                </div>
              </div>
              
              <div className="flex justify-end space-x-3">
                <button
                  type="button"
                  onClick={() => setShowForm(false)}
                  className="btn btn-secondary"
                  disabled={loading}
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  className="btn btn-primary"
                  disabled={loading}
                >
                  {loading ? (
                    <span className="flex items-center">
                      <div className="animate-spin rounded-full h-4 w-4 border-t-2 border-b-2 border-white mr-2"></div>
                      Saving...
                    </span>
                  ) : (
                    'Save Skill'
                  )}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default MySkills;