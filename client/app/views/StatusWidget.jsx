import React from 'react';
import AltContainer from 'alt-container';

import VertxActions from '../actions/VertxActions';
import StatusStore from '../stores/StatusStore';
import VertxStore from '../stores/VertxStore';

class WidgetView extends React.Component {

  render() {
    return (
      <div className="ui green mini label">
        {this.props.server.gameStatus}
      </div>
    );
  }
}

export default class StatusWidget extends React.Component {
  static getStores(props) {
    return [GameStateStore]
  }

  static getPropsFromStores(props) {
    return GameStateStore.getState()
  }

  render() {
    return (
      <div className="row">
        <AltContainer stores={{vertx: VertxStore, server: StatusStore}}>
          <WidgetView />
        </AltContainer>
      </div>
    );
  }
}
