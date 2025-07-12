import api from './api';

const userService = {
  // Get all public users
  getAllPublicUsers: async () => {
    try {
      const response = await api.get('/users/public');
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Search users by offered skill
  searchUsersByOfferedSkill: async (skillName) => {
    try {
      const response = await api.get(`/users/search/offered-skills?skillName=${encodeURIComponent(skillName)}`);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Search users by wanted skill
  searchUsersByWantedSkill: async (skillName) => {
    try {
      const response = await api.get(`/users/search/wanted-skills?skillName=${encodeURIComponent(skillName)}`);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Get user by ID
  getUserById: async (userId) => {
    try {
      const response = await api.get(`/users/${userId}`);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Get current user profile
  getCurrentUserProfile: async () => {
    try {
      const response = await api.get('/users/profile');
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Update user profile
  updateUserProfile: async (userData) => {
    try {
      const response = await api.put('/users/profile', userData);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Admin: Deactivate user
  deactivateUser: async (userId) => {
    try {
      const response = await api.put(`/users/${userId}/deactivate`);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Admin: Activate user
  activateUser: async (userId) => {
    try {
      const response = await api.put(`/users/${userId}/activate`);
      return response.data;
    } catch (error) {
      throw error;
    }
  }
};

export default userService;