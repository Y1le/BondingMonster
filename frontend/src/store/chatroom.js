const ModuleEnglistDanceGame = {
  state: {
    status: "list", // list, in-room
    socket: null, // WebSocket 实例
    currentRoomId: null, // 当前所在房间 ID
    currentUserCount: 0, // 当前房间人数
    maxUsers: 2, // 房间最大人数
    users: [], // 房间内用户列表
    context: [], // 聊天内容,用于存储多条消息
  },
  getters: {
  },
  mutations: {
    updateStatus(state, status) {
      state.status = status;
    },
    updateSocket(state, socket) {
      state.socket = socket;
    },
    updateRoom(state, room){
      state.currentRoomId = room.id;
      state.maxUsers = room.maxUsers;
    },
    updateRoomUsers(state, users) {
      state.users = users;
      state.currentUserCount = users.length; // 自动更新当前人数
    },
    /** 
     * message: 
     * { 
     * senderId: 'user1',
     *  senderName: 'Alice', 
     * text: 'Hello!', 
     * timestamp: Date.now() }
     * 
    */
    appendMessage(state, message) {
      state.context.push(message); 
    },
    removeCurrentRoom(state) {
      state.currentRoomId = null;
      state.users = [];
      state.currentUserCount = 0;
      state.status = "list"; // 离开房间后状态变为 list
      state.context = []; // 离开房间时清空聊天记录
    },
    
  },
  actions: {
    
  },
  modules: {
  }
};

export default ModuleEnglistDanceGame;
