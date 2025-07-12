import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import swapRequestService from '../services/swapRequestService';
import { useAuth } from '../context/AuthContext';

const SwapRequestDetail = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const { user } = useAuth();
  const [swapRequest, setSwapRequest] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [message, setMessage] = useState('');

  // Dummy data for the swap request
  const dummySwapRequests = [
    {
      id: 1,
      requesterId: 1,
      requesterUsername: 'johndoe',
      requesterName: 'John Doe',
      providerId: 2,
      providerUsername: 'janedoe',
      providerName: 'Jane Doe',
      requestedSkillId: 1,
      requestedSkillName: 'JavaScript Programming',
      offeredSkillId: 2,
      offeredSkillName: 'Graphic Design',
      requestDate: '2023-06-15T10:30:00',
      responseDate: null,
      status: 'PENDING',
      message: 'I would like to learn JavaScript from you. I can teach you graphic design in return.'
    },
    {
      id: 2,
      requesterId: 3,
      requesterUsername: 'mikebrown',
      requesterName: 'Mike Brown',
      providerId: 1,
      providerUsername: 'johndoe',
      providerName: 'John Doe',
      requestedSkillId: 3,
      requestedSkillName: 'Spanish Language',
      offeredSkillId: 4,
      offeredSkillName: 'Piano Lessons',
      requestDate: '2023-06-16T14:45:00',
      responseDate: '2023-06-17T09:30:00',
      status: 'ACCEPTED',
      message: 'I would love to learn Spanish. I can teach you piano in exchange.'
    },
    {
      id: 3,
      requesterId: 4,
      requesterUsername: 'sarahsmith',
      requesterName: 'Sarah Smith',
      providerId: 5,
      providerUsername: 'davidwilson',
      providerName: 'David Wilson',
      requestedSkillId: 5,
      requestedSkillName: 'Photography',
      offeredSkillId: 6,
      offeredSkillName: 'Cooking',
      requestDate: '2023-06-17T09:15:00',
      responseDate: '2023-06-18T11:20:00',
      status: 'COMPLETED',
      message: 'I want to improve my photography skills. I can teach you some amazing recipes.'
    },
    {
      id: 4,
      requesterId: 6,
      requesterUsername: 'emilyjones',
      requesterName: 'Emily Jones',
      providerId: 3,
      providerUsername: 'mikebrown',
      providerName: 'Mike Brown',
      requestedSkillId: 7,
      requestedSkillName: 'Yoga',
      offeredSkillId: 8,
      offeredSkillName: 'Digital Marketing',
      requestDate: '2023-06-18T16:30:00',
      responseDate: '2023-06-19T10:15:00',
      status: 'REJECTED',
      message: 'I need help with yoga practice. I can help you with digital marketing strategies.'
    },
    {
      id: 5,
      requesterId: 5,
      requesterUsername: 'davidwilson',
      requesterName: 'David Wilson',
      providerId: 6,
      providerUsername: 'emilyjones',
      providerName: 'Emily Jones',
      requestedSkillId: 9,
      requestedSkillName: 'Web Design',
      offeredSkillId: 10,
      offeredSkillName: 'Data Analysis',
      requestDate: '2023-06-19T11:00:00',
      responseDate: null,
      status: 'PENDING',
      message: 'I would like to learn web design. I can teach you data analysis using Python and R.'
    }
  ];

  useEffect(() => {
    // In a real application, we would fetch the data from the API
    // For now, we'll use the dummy data
    const foundRequest = dummySwapRequests.find(req => req.id === parseInt(id));
    
    if (foundRequest) {
      setSwapRequest(foundRequest);
      setLoading(false);
    } else {
      setError('Swap request not found');
      setLoading(false);
    }
  }, [id]);

  const handleAccept = async () => {
    try {
      // In a real application, we would call the API
      // For now, we'll just update the local state
      setSwapRequest({
        ...swapRequest,
        status: 'ACCEPTED',
        responseDate: new Date().toISOString()
      });
      alert('Swap request accepted successfully!');
    } catch (err) {
      setError('Failed to accept swap request');
      console.error(err);
    }
  };

  const handleReject = async () => {
    try {
      // In a real application, we would call the API
      // For now, we'll just update the local state
      setSwapRequest({
        ...swapRequest,
        status: 'REJECTED',
        responseDate: new Date().toISOString()
      });
      alert('Swap request rejected successfully!');
    } catch (err) {
      setError('Failed to reject swap request');
      console.error(err);
    }
  };

  const handleComplete = async () => {
    try {
      // In a real application, we would call the API
      // For now, we'll just update the local state
      setSwapRequest({
        ...swapRequest,
        status: 'COMPLETED',
        responseDate: new Date().toISOString()
      });
      alert('Swap request marked as completed!');
    } catch (err) {
      setError('Failed to complete swap request');
      console.error(err);
    }
  };

  const handleCancel = async () => {
    try {
      // In a real application, we would call the API
      // For now, we'll just update the local state
      setSwapRequest({
        ...swapRequest,
        status: 'CANCELLED',
        responseDate: new Date().toISOString()
      });
      alert('Swap request cancelled successfully!');
    } catch (err) {
      setError('Failed to cancel swap request');
      console.error(err);
    }
  };

  const handleSendMessage = () => {
    if (!message.trim()) return;
    
    // In a real application, we would send the message to the API
    // For now, we'll just show an alert
    alert(`Message sent: ${message}`);
    setMessage('');
  };

  const isRequester = swapRequest?.requesterUsername === user?.username;
  const isProvider = swapRequest?.providerUsername === user?.username;
  const isParticipant = isRequester || isProvider;

  if (loading) return <div className="text-center p-8">Loading swap request details...</div>;
  if (error) return <div className="text-center p-8 text-red-500">Error: {error}</div>;
  if (!swapRequest) return <div className="text-center p-8">Swap request not found</div>;

  return (
    <div className="container mx-auto p-6">
      <button
        onClick={() => navigate('/swap-requests')}
        className="mb-6 flex items-center text-blue-600 hover:text-blue-800"
      >
        <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5 mr-1" viewBox="0 0 20 20" fill="currentColor">
          <path fillRule="evenodd" d="M9.707 14.707a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414l4-4a1 1 0 011.414 1.414L7.414 9H15a1 1 0 110 2H7.414l2.293 2.293a1 1 0 010 1.414z" clipRule="evenodd" />
        </svg>
        Back to Swap Requests
      </button>
      
      <div className="bg-white p-6 rounded-lg shadow-md mb-6">
        <div className="flex justify-between items-start mb-6">
          <h1 className="text-3xl font-bold">Swap Request Details</h1>
          <span className={`px-3 py-1 rounded-full text-sm font-medium ${
            swapRequest.status === 'PENDING' ? 'bg-yellow-100 text-yellow-800' :
            swapRequest.status === 'ACCEPTED' ? 'bg-green-100 text-green-800' :
            swapRequest.status === 'COMPLETED' ? 'bg-blue-100 text-blue-800' :
            swapRequest.status === 'REJECTED' ? 'bg-red-100 text-red-800' :
            'bg-gray-100 text-gray-800'
          }`}>
            {swapRequest.status}
          </span>
        </div>
        
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-6">
          <div>
            <h2 className="text-xl font-semibold mb-4">Requester</h2>
            <div className="bg-gray-50 p-4 rounded">
              <p className="text-lg font-medium">{swapRequest.requesterName}</p>
              <p className="text-gray-600">@{swapRequest.requesterUsername}</p>
            </div>
          </div>
          
          <div>
            <h2 className="text-xl font-semibold mb-4">Provider</h2>
            <div className="bg-gray-50 p-4 rounded">
              <p className="text-lg font-medium">{swapRequest.providerName}</p>
              <p className="text-gray-600">@{swapRequest.providerUsername}</p>
            </div>
          </div>
        </div>
        
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-6">
          <div>
            <h2 className="text-xl font-semibold mb-4">Requested Skill</h2>
            <div className="bg-gray-50 p-4 rounded">
              <p className="text-lg">{swapRequest.requestedSkillName}</p>
            </div>
          </div>
          
          <div>
            <h2 className="text-xl font-semibold mb-4">Offered Skill</h2>
            <div className="bg-gray-50 p-4 rounded">
              <p className="text-lg">{swapRequest.offeredSkillName}</p>
            </div>
          </div>
        </div>
        
        <div className="mb-6">
          <h2 className="text-xl font-semibold mb-4">Message</h2>
          <div className="bg-gray-50 p-4 rounded">
            <p className="text-gray-700">{swapRequest.message}</p>
          </div>
        </div>
        
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-6">
          <div>
            <h2 className="text-xl font-semibold mb-4">Request Date</h2>
            <div className="bg-gray-50 p-4 rounded">
              <p className="text-gray-700">{new Date(swapRequest.requestDate).toLocaleString()}</p>
            </div>
          </div>
          
          {swapRequest.responseDate && (
            <div>
              <h2 className="text-xl font-semibold mb-4">Response Date</h2>
              <div className="bg-gray-50 p-4 rounded">
                <p className="text-gray-700">{new Date(swapRequest.responseDate).toLocaleString()}</p>
              </div>
            </div>
          )}
        </div>
        
        {/* Action buttons based on status and user role */}
        {isParticipant && swapRequest.status === 'PENDING' && (
          <div className="flex flex-wrap gap-4 mb-6">
            {isProvider && (
              <>
                <button
                  onClick={handleAccept}
                  className="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700"
                >
                  Accept Request
                </button>
                <button
                  onClick={handleReject}
                  className="bg-red-600 text-white px-4 py-2 rounded hover:bg-red-700"
                >
                  Reject Request
                </button>
              </>
            )}
            {isRequester && (
              <button
                onClick={handleCancel}
                className="bg-gray-600 text-white px-4 py-2 rounded hover:bg-gray-700"
              >
                Cancel Request
              </button>
            )}
          </div>
        )}
        
        {isParticipant && swapRequest.status === 'ACCEPTED' && (
          <div className="flex flex-wrap gap-4 mb-6">
            <button
              onClick={handleComplete}
              className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
            >
              Mark as Completed
            </button>
            <button
              onClick={handleCancel}
              className="bg-gray-600 text-white px-4 py-2 rounded hover:bg-gray-700"
            >
              Cancel Request
            </button>
          </div>
        )}
        
        {/* Message form */}
        {isParticipant && (swapRequest.status === 'PENDING' || swapRequest.status === 'ACCEPTED') && (
          <div className="mt-8">
            <h2 className="text-xl font-semibold mb-4">Send a Message</h2>
            <div className="flex flex-col md:flex-row gap-4">
              <textarea
                className="flex-grow p-2 border rounded"
                rows="3"
                placeholder="Type your message here..."
                value={message}
                onChange={(e) => setMessage(e.target.value)}
              ></textarea>
              <button
                onClick={handleSendMessage}
                className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 md:self-end"
              >
                Send Message
              </button>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default SwapRequestDetail;