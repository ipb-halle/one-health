import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

import { orcidLogin } from '@/generated/auth/auth/auth';
import { authService } from '@/app/services/auth.service';

const OrcidCallbackPageComponent: React.FC = () => {
    const navigate = useNavigate();

    useEffect(() => {
        const authenticate = async () => {
            const params = new URLSearchParams(window.location.search);
            const code = params.get('code');
            const state = params.get('state') || sessionStorage.getItem('orcid_state');

            if (!code || !state) {
                console.error('Missing ORCID authorization code or state.');
                return;
            }
            try {
                const response = await orcidLogin({
                    code,
                    state,
                });
                authService.setSession(response);
                sessionStorage.removeItem('orcid_state');
                navigate('/');
            } catch (error) {
                console.error('ORCID authentication failed:', error);
            }
        };
        authenticate();
    }, [navigate]);

    return <div>Signing in with ORCID...</div>;
};

export default OrcidCallbackPageComponent;