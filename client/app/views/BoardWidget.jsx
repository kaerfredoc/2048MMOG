import React from 'react';
import AltContainer from 'alt-container';

import VertxActions from '../actions/VertxActions';
import GameStateStore from '../stores/GameStateStore';
import VertxStore from '../stores/VertxStore';

class WidgetView extends React.Component {

  render() {
    return (
      <div className="">
        {this.props.server.board}
        <div className="heading">
          <h1 className="title">2048</h1>
          <div className="scores-container">
            <div className="score-container">0</div>
            <div className="best-container">0</div>
          </div>
        </div>

        <div className="above-game">
          <p className="game-intro">Join the numbers and get to the <strong>2048 tile!</strong></p>
          <a className="restart-button">New Game</a>
        </div>

        <div className="game-container">
          <div className="game-message">
            <p></p>
            <div className="lower">
              <a className="keep-playing-button">Keep going</a>
              <a className="retry-button">Try again</a>
            </div>
          </div>

          <div className="grid-container">
            <div className="grid-row">
              <div className="grid-cell"></div>
              <div className="grid-cell"></div>
              <div className="grid-cell"></div>
              <div className="grid-cell"></div>
            </div>
            <div className="grid-row">
              <div className="grid-cell"></div>
              <div className="grid-cell"></div>
              <div className="grid-cell"></div>
              <div className="grid-cell"></div>
            </div>
            <div className="grid-row">
              <div className="grid-cell"></div>
              <div className="grid-cell"></div>
              <div className="grid-cell"></div>
              <div className="grid-cell"></div>
            </div>
            <div className="grid-row">
              <div className="grid-cell"></div>
              <div className="grid-cell"></div>
              <div className="grid-cell"></div>
              <div className="grid-cell"></div>
            </div>
          </div>

          <div className="tile-container">

          </div>
        </div>

        <p className="game-explanation">
          <strong className="important">How to play:</strong> Use your <strong>arrow keys</strong> to move the tiles.
          When two tiles with the same number touch, they <strong>merge into one!</strong>
        </p>
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
    return (
      <div className="ui segment">
        <AltContainer stores={{vertx: VertxStore, server: GameStateStore}}>
          <WidgetView />
        </AltContainer>
      </div>
    );
  }
}
