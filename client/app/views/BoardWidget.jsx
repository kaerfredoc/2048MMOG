import React from 'react/addons';
import AltContainer from 'alt-container';
import classSet from 'classnames';

import VertxActions from '../actions/VertxActions.js';
import GameStateStore from '../stores/GameStateStore';

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

  render() {
    return (
      <div className="ui segment">
        <AltContainer stores={{server: GameStateStore}}>
          <WidgetView />
        </AltContainer>
      </div>
    );
  }

}