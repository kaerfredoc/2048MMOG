import alt from '../libs/alt';

class VertxActions {
  constructor() {
    this.generateActions('statusUpdate', 'gameUpdate', 'move', 'register',
      'vertxReady', 'vertxUnready', 'vertxError', 'vertxConnect', 'vertxDisconnect')
  }
}

export default alt.createActions(VertxActions);