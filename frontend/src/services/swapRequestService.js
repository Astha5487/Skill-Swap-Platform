import api from './api';

const swapRequestService = {
  // Get current user's swap requests
  getCurrentUserSwapRequests: async () => {
    try {
      const response = await api.get('/swap-requests');
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Get sent swap requests
  getSentSwapRequests: async () => {
    try {
      const response = await api.get('/swap-requests/sent');
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Get received swap requests
  getReceivedSwapRequests: async () => {
    try {
      const response = await api.get('/swap-requests/received');
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Get swap requests by status
  getSwapRequestsByStatus: async (status) => {
    try {
      const response = await api.get(`/swap-requests/status/${status}`);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Get swap request by ID
  getSwapRequestById: async (id) => {
    try {
      const response = await api.get(`/swap-requests/${id}`);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Create a new swap request
  createSwapRequest: async (swapRequestData) => {
    try {
      const response = await api.post('/swap-requests', swapRequestData);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Accept a swap request
  acceptSwapRequest: async (id) => {
    try {
      const response = await api.put(`/swap-requests/${id}/accept`);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Reject a swap request
  rejectSwapRequest: async (id) => {
    try {
      const response = await api.put(`/swap-requests/${id}/reject`);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Complete a swap request
  completeSwapRequest: async (id) => {
    try {
      const response = await api.put(`/swap-requests/${id}/complete`);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Cancel a swap request
  cancelSwapRequest: async (id) => {
    try {
      const response = await api.put(`/swap-requests/${id}/cancel`);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Admin: Get all swap requests
  getAllSwapRequests: async () => {
    try {
      const response = await api.get('/swap-requests/all');
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Admin: Get all swap requests by status
  getAllSwapRequestsByStatus: async (status) => {
    try {
      const response = await api.get(`/swap-requests/all/status/${status}`);
      return response.data;
    } catch (error) {
      throw error;
    }
  }
};

export default swapRequestService;