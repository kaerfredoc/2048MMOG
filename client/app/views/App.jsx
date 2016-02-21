import React from 'react';
import AltContainer from 'alt-container';
import BoardWidget from './BoardWidget';
import StatusWidget from './StatusWidget';
import VertxStore from '../stores/VertxStore.js';
import VertxActions from '../actions/VertxActions';

export default class App extends React.Component {

  render() {
    return (
      <div className="container">
        <StatusWidget/>
        <BoardWidget/>
      </div>
    );
  }

}