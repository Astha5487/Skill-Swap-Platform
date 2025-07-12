import api from './api';

const feedbackService = {
  // Get feedback for a user
  getUserFeedback: async (userId) => {
    try {
      const response = await api.get(`/feedback/user/${userId}`);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Get average rating for a user
  getUserRating: async (userId) => {
    try {
      const response = await api.get(`/feedback/rating/${userId}`);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Get feedback given by a user
  getFeedbackGivenByUser: async (userId) => {
    try {
      const response = await api.get(`/feedback/given-by/${userId}`);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Get feedback for a swap request
  getSwapRequestFeedback: async (swapRequestId) => {
    try {
      const response = await api.get(`/feedback/swap-request/${swapRequestId}`);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Create feedback for a swap request
  createFeedback: async (feedbackData) => {
    try {
      const response = await api.post('/feedback', feedbackData);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Update feedback
  updateFeedback: async (feedbackId, feedbackData) => {
    try {
      const response = await api.put(`/feedback/${feedbackId}`, feedbackData);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Delete feedback
  deleteFeedback: async (feedbackId) => {
    try {
      const response = await api.delete(`/feedback/${feedbackId}`);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Admin: Get all feedback
  getAllFeedback: async () => {
    try {
      const response = await api.get('/feedback/all');
      return response.data;
    } catch (error) {
      throw error;
    }
  }
};

export default feedbackService;
