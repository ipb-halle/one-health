import React from 'react';
import { Card } from 'primereact/card';
import { InputText } from 'primereact/inputtext';
import { Password } from 'primereact/password';
import { Button } from 'primereact/button';

const LoginPage: React.FC = () => {
    return (
        <div
            style={{
                display: 'flex',
                justifyContent: 'center',
                alignItems: 'center',
                minHeight: '70vh',
            }}
        >
            <Card
                title="Login"
                style={{
                    width: '400px',
                }}
            >
                <div
                    style={{
                        display: 'flex',
                        flexDirection: 'column',
                        gap: '1rem',
                    }}
                >
                    <span className="p-float-label">
                        <InputText
                            id="username"
                            className="w-full"
                        />
                        <label htmlFor="username">
                            Username
                        </label>
                    </span>

                    <span className="p-float-label">
                        <Password
                            id="password"
                            feedback={false}
                            toggleMask
                            className="w-full"
                            inputClassName="w-full"
                        />
                        <label htmlFor="password">
                            Password
                        </label>
                    </span>

                    <Button
                        label="Login"
                        icon="pi pi-sign-in"
                    />
                </div>
            </Card>
        </div>
    );
};

export default LoginPage;