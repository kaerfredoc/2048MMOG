import alt from '../libs/alt';

class VertxActions {

    gameUpdate(message) {
        this.dispatch(message);
    }

    vertxReady(eventBus) {
        this.dispatch(eventBus);
    }

    vertxUnready(eventBus) {
        this.dispatch(eventBus);
    }

    vertxError(err) {
        this.dispatch(err);
    }

    doVertxConnect() {
        this.dispatch();
    }

    doVertxDisconnect() {
        this.dispatch();
    }
}

export default  alt.createActions(VertxActions);