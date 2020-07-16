package com.bitmovin.analytics.stateMachines;

import com.bitmovin.analytics.utils.Util;

public enum PlayerState {
    READY {
        @Override
        void onEnterState(PlayerStateMachine machine) {
        }

        @Override
        void onExitState(PlayerStateMachine machine, long elapsedTime, PlayerState desintationPlayerState) {
        }
    },
    STARTUP {
        @Override
        void onEnterState(PlayerStateMachine machine) {
            machine.videoStartTimeout.start();
        }

        @Override
        void onExitState(PlayerStateMachine machine, long elapsedTime, PlayerState destinationPlayerState) {
            machine.videoStartTimeout.cancel();
            long elapsedTimeOnEnter = machine.getElapsedTimeOnEnter();
            machine.addStartupTime(elapsedTime - elapsedTimeOnEnter);
            if (destinationPlayerState == PlayerState.PLAYING) {
                for (StateMachineListener listener : machine.getListeners()) {
                    listener.onStartup(machine.getStartupTime());
                }
                machine.setStartupFinished(true);
            }
        }
    },
    AD {
        @Override
        void onEnterState(PlayerStateMachine machine) {
        }

        @Override
        void onExitState(PlayerStateMachine machine, long elapsedTime, PlayerState desintationPlayerState) {
        }
    },
    BUFFERING {
        @Override
        void onEnterState(PlayerStateMachine machine) {
            machine.enableRebufferHeartbeat();
        }

        @Override
        void onExitState(PlayerStateMachine machine, long elapsedTime, PlayerState desintationPlayerState) {
            machine.disableRebufferHeartbeat();
            for (StateMachineListener listener : machine.getListeners()) {
                long elapsedTimeOnEnter = machine.getElapsedTimeOnEnter();
                listener.onRebuffering(elapsedTime - elapsedTimeOnEnter);
            }
        }
    },
    ERROR {
        @Override
        void onEnterState(PlayerStateMachine machine) {
            for (StateMachineListener listener : machine.getListeners()) {
                listener.onError(machine.getErrorCode());
            }
        }

        @Override
        void onExitState(PlayerStateMachine machine, long elapsedTime, PlayerState desintationPlayerState) {
            machine.setVideoStartFailedReason(null);
        }
    },
    EXITBEFOREVIDEOSTART {
        @Override
        void onEnterState(PlayerStateMachine machine) {
            for (StateMachineListener listener : machine.getListeners()) {
                listener.onVideoStartFailed();
            }
        }

        @Override
        void onExitState(PlayerStateMachine machine, long elapsedTime, PlayerState desintationPlayerState) {
            machine.setVideoStartFailedReason(null);
        }
    },
    PLAYING {
        @Override
        void onEnterState(PlayerStateMachine machine) {
            machine.enableHeartbeat();
        }

        @Override
        void onExitState(PlayerStateMachine machine, long elapsedTime, PlayerState desintationPlayerState) {
            for (StateMachineListener listener : machine.getListeners()) {
                long elapsedTimeOnEnter = machine.getElapsedTimeOnEnter();
                listener.onPlayExit(elapsedTime - elapsedTimeOnEnter);
            }

            machine.disableHeartbeat();

        }
    },
    PAUSE {
        @Override
        void onEnterState(PlayerStateMachine machine) {
        }

        @Override
        void onExitState(PlayerStateMachine machine, long elapsedTime, PlayerState desintationPlayerState) {
            for (StateMachineListener listener : machine.getListeners()) {
                long elapsedTimeOnEnter = machine.getElapsedTimeOnEnter();
                listener.onPauseExit(elapsedTime - elapsedTimeOnEnter);
            }
        }
    },
    QUALITYCHANGE {
        @Override
        void onEnterState(PlayerStateMachine machine) {
        }

        @Override
        void onExitState(PlayerStateMachine machine, long elapsedTime, PlayerState destinationPlayerState) {
            for (StateMachineListener listener : machine.getListeners()) {
                listener.onQualityChange();
            }
        }
    },
    AUDIOTRACKCHANGE {
        @Override
        void onEnterState(PlayerStateMachine machine) {
        }

        @Override
        void onExitState(PlayerStateMachine machine, long elapsedTime, PlayerState destinationPlayerState) {
            for (StateMachineListener listener : machine.getListeners()) {
                listener.onAudioTrackChange();
            }
        }
    },
    SUBTITLECHANGE {
        @Override
        void onEnterState(PlayerStateMachine machine) {
        }

        @Override
        void onExitState(PlayerStateMachine machine, long elapsedTime, PlayerState destinationPlayerState) {
            for (StateMachineListener listener : machine.getListeners()) {
                listener.onSubtitleChange();
            }
        }
    },

    SEEKING {
        @Override
        void onEnterState(PlayerStateMachine machine) {
            machine.setElapsedTimeSeekStart(machine.getElapsedTimeOnEnter());
        }

        @Override
        void onExitState(PlayerStateMachine machine, long elapsedTime, PlayerState destinationPlayerState) {
            for (StateMachineListener listener : machine.getListeners()) {
                listener.onSeekComplete(elapsedTime - machine.getElapsedTimeSeekStart());
            }
            machine.setElapsedTimeSeekStart(0);
        }
    };

    abstract void onEnterState(PlayerStateMachine machine);

    abstract void onExitState(PlayerStateMachine machine, long elapsedTime, PlayerState desintationPlayerState);

}
