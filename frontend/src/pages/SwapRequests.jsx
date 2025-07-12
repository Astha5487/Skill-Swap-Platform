import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import swapRequestService from '../services/swapRequestService';
import { useAuth } from '../context/AuthContext';

const SwapRequests = () => {
  const { user } = useAuth();
  const [swapRequests, setSwapRequests] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [activeTab, setActiveTab] = useState('all');
  const [requestSkill, setRequestSkill] = useState('');

  // Dummy data for swap requests
  const dummySwapRequests = [
    {
      id: 1,
      requesterId: 1,
      requesterUsername: 'johndoe',
      providerId: 2,
      providerUsername: 'janedoe',
      requestedSkillId: 1,
      requestedSkillName: 'JavaScript Programming',
      offeredSkillId: 2,
      offeredSkillName: 'Graphic Design',
      requestDate: '2023-06-15T10:30:00',
      status: 'PENDING',
      message: 'I would like to learn JavaScript from you. I can teach you graphic design in return.'
    },
    {
      id: 2,
      requesterId: 3,
      requesterUsername: 'mikebrown',
      providerId: 1,
      providerUsername: 'johndoe',
      requestedSkillId: 3,
      requestedSkillName: 'Spanish Language',
      offeredSkillId: 4,
      offeredSkillName: 'Piano Lessons',
      requestDate: '2023-06-16T14:45:00',
      status: 'ACCEPTED',
      message: 'I would love to learn Spanish. I can teach you piano in exchange.'
    },
    {
      id: 3,
      requesterId: 4,
      requesterUsername: 'sarahsmith',
      providerId: 5,
      providerUsername: 'davidwilson',
      requestedSkillId: 5,
      requestedSkillName: 'Photography',
      offeredSkillId: 6,
      offeredSkillName: 'Cooking',
      requestDate: '2023-06-17T09:15:00',
      status: 'COMPLETED',
      message: 'I want to improve my photography skills. I can teach you some amazing recipes.'
    },
    {
      id: 4,
      requesterId: 6,
      requesterUsername: 'emilyjones',
      providerId: 3,
      providerUsername: 'mikebrown',
      requestedSkillId: 7,
      requestedSkillName: 'Yoga',
      offeredSkillId: 8,
      offeredSkillName: 'Digital Marketing',
      requestDate: '2023-06-18T16:30:00',
      status: 'REJECTED',
      message: 'I need help with yoga practice. I can help you with digital marketing strategies.'
    },
    {
      id: 5,
      requesterId: 5,
      requesterUsername: 'davidwilson',
      providerId: 6,
      providerUsername: 'emilyjones',
      requestedSkillId: 9,
      requestedSkillName: 'Web Design',
      offeredSkillId: 10,
      offeredSkillName: 'Data Analysis',
      requestDate: '2023-06-19T11:00:00',
      status: 'PENDING',
      message: 'I would like to learn web design. I can teach you data analysis using Python and R.'
    }
  ];

  useEffect(() => {
    // In a real application, we would fetch the data from the API
    // For now, we'll use the dummy data
    setSwapRequests(dummySwapRequests);
    setLoading(false);
  }, []);

  const handleTabChange = (tab) => {
    setActiveTab(tab);
  };

  const filteredRequests = () => {
    switch (activeTab) {
      case 'sent':
        return dummySwapRequests.filter(req => req.requesterUsername === user?.username);
      case 'received':
        return dummySwapRequests.filter(req => req.providerUsername === user?.username);
      case 'pending':
        return dummySwapRequests.filter(req => req.status === 'PENDING');
      case 'accepted':
        return dummySwapRequests.filter(req => req.status === 'ACCEPTED');
      case 'completed':
        return dummySwapRequests.filter(req => req.status === 'COMPLETED');
      default:
        return dummySwapRequests;
    }
  };

  const handleRequestSkill = () => {
    alert(`You requested the skill: ${requestSkill}`);
    setRequestSkill('');
  };

  if (loading) return <div className="text-center p-8">Loading swap requests...</div>;
  if (error) return <div className="text-center p-8 text-red-500">Error: {error}</div>;

  return (
    <div className="container mx-auto p-6">
      <h1 className="text-3xl font-bold mb-6">Swap Requests</h1>
      
      {/* Request Skill Form */}
      <div className="bg-white p-6 rounded-lg shadow-md mb-6">
        <h2 className="text-xl font-semibold mb-4">Request a New Skill</h2>
        <div className="flex flex-col md:flex-row gap-4">
          <input
            type="text"
            placeholder="Enter the skill you want to request..."
            className="flex-grow p-2 border rounded"
            value={requestSkill}
            onChange={(e) => setRequestSkill(e.target.value)}
          />
          <button
            onClick={handleRequestSkill}
            className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
          >
            Request Skill
          </button>
        </div>
      </div>
      
      {/* Tabs */}
      <div className="flex flex-wrap gap-2 mb-6">
        <button
          className={`px-4 py-2 rounded ${activeTab === 'all' ? 'bg-blue-600 text-white' : 'bg-gray-200'}`}
          onClick={() => handleTabChange('all')}
        >
          All
        </button>
        <button
          className={`px-4 py-2 rounded ${activeTab === 'sent' ? 'bg-blue-600 text-white' : 'bg-gray-200'}`}
          onClick={() => handleTabChange('sent')}
        >
          Sent
        </button>
        <button
          className={`px-4 py-2 rounded ${activeTab === 'received' ? 'bg-blue-600 text-white' : 'bg-gray-200'}`}
          onClick={() => handleTabChange('received')}
        >
          Received
        </button>
        <button
          className={`px-4 py-2 rounded ${activeTab === 'pending' ? 'bg-blue-600 text-white' : 'bg-gray-200'}`}
          onClick={() => handleTabChange('pending')}
        >
          Pending
        </button>
        <button
          className={`px-4 py-2 rounded ${activeTab === 'accepted' ? 'bg-blue-600 text-white' : 'bg-gray-200'}`}
          onClick={() => handleTabChange('accepted')}
        >
          Accepted
        </button>
        <button
          className={`px-4 py-2 rounded ${activeTab === 'completed' ? 'bg-blue-600 text-white' : 'bg-gray-200'}`}
          onClick={() => handleTabChange('completed')}
        >
          Completed
        </button>
      </div>
      
      {/* Swap Requests List */}
      <div className="grid grid-cols-1 gap-6">
        {filteredRequests().length > 0 ? (
          filteredRequests().map((request) => (
            <div key={request.id} className="bg-white p-6 rounded-lg shadow-md">
              <div className="flex justify-between items-start mb-4">
                <div>
                  <h2 className="text-xl font-semibold">
                    {request.requesterUsername} ↔ {request.providerUsername}
                  </h2>
                  <p className="text-gray-600">
                    Requested on {new Date(request.requestDate).toLocaleDateString()}
                  </p>
                </div>
                <span className={`px-3 py-1 rounded-full text-sm font-medium ${
                  request.status === 'PENDING' ? 'bg-yellow-100 text-yellow-800' :
                  request.status === 'ACCEPTED' ? 'bg-green-100 text-green-800' :
                  request.status === 'COMPLETED' ? 'bg-blue-100 text-blue-800' :
                  'bg-red-100 text-red-800'
                }`}>
                  {request.status}
                </span>
              </div>
              
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-4">
                <div className="bg-gray-50 p-4 rounded">
                  <h3 className="font-medium text-gray-700">Requested Skill</h3>
                  <p className="text-lg">{request.requestedSkillName}</p>
                </div>
                <div className="bg-gray-50 p-4 rounded">
                  <h3 className="font-medium text-gray-700">Offered Skill</h3>
                  <p className="text-lg">{request.offeredSkillName}</p>
                </div>
              </div>
              
              <div className="mb-4">
                <h3 className="font-medium text-gray-700">Message</h3>
                <p className="text-gray-600">{request.message}</p>
              </div>
              
              <div className="flex justify-end">
                <Link
                  to={`/swap-requests/${request.id}`}
                  className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
                >
                  View Details
                </Link>
              </div>
            </div>
          ))
        ) : (
          <div className="text-center p-8 bg-white rounded-lg shadow-md">
            No swap requests found for this filter.
          </div>
        )}
      </div>
    </div>
  );
};

export default SwapRequests;