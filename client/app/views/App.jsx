import React from 'react';
import AltContainer from 'alt-container';
import BoardWidget from './BoardWidget';
import StatusWidget from './StatusWidget';
import TopScoresWidget from './TopScoresWidget';
import RegistrationForm from './RegistrationForm';
import VertxStore from '../stores/VertxStore.js';
import VertxActions from '../actions/VertxActions';

export default class App extends React.Component {

  render() {
    return (
      <div className="container">
        <StatusWidget/>
        <RegistrationForm/>
        <TopScoresWidget/>
        <BoardWidget/>
      </div>
    );
  }

}