import api from './api';

const skillService = {
  // Get all skill names
  getAllSkillNames: async () => {
    try {
      const response = await api.get('/skills');
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Search skills by name
  searchSkillsByName: async (name) => {
    try {
      const response = await api.get(`/skills/search?name=${encodeURIComponent(name)}`);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Search offered skills by name
  searchOfferedSkillsByName: async (name) => {
    try {
      const response = await api.get(`/skills/search/offered?name=${encodeURIComponent(name)}`);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Search wanted skills by name
  searchWantedSkillsByName: async (name) => {
    try {
      const response = await api.get(`/skills/search/wanted?name=${encodeURIComponent(name)}`);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Get skills by user
  getSkillsByUser: async (userId) => {
    try {
      const response = await api.get(`/skills/user/${userId}`);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Get offered skills by user
  getOfferedSkillsByUser: async (userId) => {
    try {
      const response = await api.get(`/skills/user/${userId}/offered`);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Get wanted skills by user
  getWantedSkillsByUser: async (userId) => {
    try {
      const response = await api.get(`/skills/user/${userId}/wanted`);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Add a new skill
  addSkill: async (skillData) => {
    try {
      const response = await api.post('/skills', skillData);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Update a skill
  updateSkill: async (skillId, skillData) => {
    try {
      const response = await api.put(`/skills/${skillId}`, skillData);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Delete a skill
  deleteSkill: async (skillId) => {
    try {
      const response = await api.delete(`/skills/${skillId}`);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Admin: Approve a skill
  approveSkill: async (skillId) => {
    try {
      const response = await api.put(`/skills/${skillId}/approve`);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Admin: Reject a skill
  rejectSkill: async (skillId) => {
    try {
      const response = await api.put(`/skills/${skillId}/reject`);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Admin: Get skills pending approval
  getPendingApprovalSkills: async () => {
    try {
      const response = await api.get('/skills/pending');
      return response.data;
    } catch (error) {
      throw error;
    }
  }
};

export default skillService;