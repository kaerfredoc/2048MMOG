import React from 'react';
import AltContainer from 'alt-container';

import VertxActions from '../actions/VertxActions';
import GameStateStore from '../stores/GameStateStore';
import VertxStore from '../stores/VertxStore';

class WidgetView extends React.Component {

  render() {
    console.log(this.props.server);

    return (
      <div className="ui green mini label">
        {this.props.server.serverTime}
      </div>
    );
  }
}

export default class BoardWidget extends React.Component {
  static getStores(props) {
    return [GameStateStore]
  }

  static getPropsFromStores(props) {
    return GameStateStore.getState()
  }

  render() {
    console.log(this.props.server);
    return (
      <div className="ui segment">
        <AltContainer stores={{vertx: VertxStore, server: GameStateStore}}>
          <WidgetView />
        </AltContainer>
      </div>
    );
  }
}
