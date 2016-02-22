import React from 'react';
import AltContainer from 'alt-container';

import VertxActions from '../actions/VertxActions';
import TopGamesStore from '../stores/TopGamesStore';
import VertxStore from '../stores/VertxStore';

class WidgetView extends React.Component {

  render() {
    return (
      <div className="ui green mini label">
        {this.props.server.boards}
      </div>
    );
  }
}

export default class TopScoresWidget extends React.Component {
  static getStores(props) {
    return [GameStateStore]
  }

  static getPropsFromStores(props) {
    return GameStateStore.getState()
  }

  render() {
    return (
      <div className="ui segment">
        <AltContainer stores={{vertx: VertxStore, server: TopGamesStore}}>
          <WidgetView />
        </AltContainer>
      </div>
    );
  }
}
